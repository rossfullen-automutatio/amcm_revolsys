package com.revolsys.transaction;

import java.lang.ScopedValue.Carrier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.jeometry.common.exception.Exceptions;
import org.jeometry.common.util.BaseCloseable;

public class Transaction {
  public interface Builder {

    <V> V call(Callable<V> action);

    void run(RunAction action);
  }

  static class MandatoryBuilder implements Builder {
    @Override
    public <V> V call(final Callable<V> action) {
      if (hasContext()) {
        try {
          return action.call();
        } catch (final Exception e) {
          return Exceptions.throwUncheckedException(e);
        }
      } else {
        throw new IllegalStateException(
          "Propagation Mandatory must run inside an existing transaction");
      }
    }

    @Override
    public void run(final RunAction action) {
      if (hasContext()) {
        try {
          action.run();
        } catch (final Exception e) {
          Exceptions.throwUncheckedException(e);
        }
      } else {
        throw new IllegalStateException(
          "Propagation Mandatory must run inside an existing transaction");
      }
    }
  }

  static class NeverBuilder implements Builder {
    @Override
    public <V> V call(final Callable<V> action) {
      if (hasContext()) {
        throw new IllegalStateException(
          "Propagation Mandatory must run inside an existing transaction");
      } else {
        try {
          return action.call();
        } catch (final Exception e) {
          return Exceptions.throwUncheckedException(e);
        }
      }
    }

    @Override
    public void run(final RunAction action) {
      if (hasContext()) {
        throw new IllegalStateException(
          "Propagation Mandatory must run inside an existing transaction");
      } else {
        try {
          action.run();
        } catch (final Exception e) {
          Exceptions.throwUncheckedException(e);
        }
      }
    }
  }

  static class NotSupportedBuilder implements Builder {
    @Override
    public <V> V call(final Callable<V> action) {
      try (
        var s = suspend()) {
        try {
          return ScopedValue.where(CONTEXT, EMPTY).call(action);
        } catch (final Throwable t) {
          return EMPTY.setRollbackOnly(t);
        }
      }
    }

    @Override
    public void run(final RunAction action) {
      try (
        var s = suspend()) {
        final Runnable runnable = runnable(EMPTY, action);
        ScopedValue.where(CONTEXT, EMPTY).run(runnable);
      }
    }
  }

  public static class RequiredBuilder implements Builder {
    private final List<Consumer<ActiveTransactionContext>> initializers = new ArrayList<>();

    public RequiredBuilder addInit(final Consumer<ActiveTransactionContext> initializer) {
      this.initializers.add(initializer);
      return this;
    }

    @Override
    public <V> V call(final Callable<V> action) {
      if (hasContext()) {
        try {
          return action.call();
        } catch (final Exception e) {
          return Exceptions.throwUncheckedException(e);
        }
      } else {
        try (
          var context = new ActiveTransactionContext(this.initializers)) {
          try {
            return ScopedValue.where(CONTEXT, context).call(action);
          } catch (final Throwable t) {
            return context.setRollbackOnly(t);
          }
        }
      }
    }

    @Override
    public void run(final RunAction action) {
      if (hasContext()) {
        try {
          action.run();
        } catch (final Exception e) {
          Exceptions.throwUncheckedException(e);
        }
      } else {
        try (
          var context = new ActiveTransactionContext(this.initializers)) {
          final Runnable runnable = runnable(context, action);
          ScopedValue.where(CONTEXT, context).run(runnable);
        }
      }
    }

  }

  public static class RequiresNewBuilder extends TransactionDefinition<RequiresNewBuilder>
    implements Builder {
    private final List<Consumer<ActiveTransactionContext>> initializers = new ArrayList<>();

    public RequiresNewBuilder addInit(final Consumer<ActiveTransactionContext> initializer) {
      this.initializers.add(initializer);
      return this;
    }

    @Override
    public <V> V call(final Callable<V> action) {
      try (
        var s = suspend();
        ActiveTransactionContext context = new ActiveTransactionContext(this, this.initializers)) {
        try {
          return ScopedValue.where(CONTEXT, context).call(action);
        } catch (final Throwable t) {
          return context.setRollbackOnly(t);
        }
      }
    }

    @Override
    public void run(final RunAction action) {
      try (
        var s = suspend();
        ActiveTransactionContext context = new ActiveTransactionContext(this, this.initializers)) {
        final Runnable runnable = runnable(context, action);
        ScopedValue.where(CONTEXT, context).run(runnable);
      }
    }
  }

  public interface RunAction {
    void run() throws Exception;
  }

  static class SupportsBuilder implements Builder {

    @Override
    public <V> V call(final Callable<V> action) {
      try {
        return action.call();
      } catch (final Exception e) {
        return Exceptions.throwUncheckedException(e);
      }
    }

    @Override
    public void run(final RunAction action) {
      try {
        action.run();
      } catch (final Exception e) {
        Exceptions.throwUncheckedException(e);
      }
    }
  }

  private static final TransactionContext EMPTY = new EmptyTransactionContext();

  static final ScopedValue<TransactionContext> CONTEXT = ScopedValue.newInstance();

  public static void afterCommit(final Runnable action) {
    final TransactionContext context = getContext();
    if (context instanceof final ActiveTransactionContext activeContext) {
      activeContext.addAfterCommit(action);
    } else {
      action.run();
    }
  }

  public static void assertInTransaction() {
    assert !isActive() : "Must be called in a transaction";
  }

  public static TransactionContext getContext() {
    return CONTEXT.orElse(EMPTY);
  }

  public static boolean hasContext() {
    return CONTEXT.isBound();
  }

  public static boolean isActive() {
    return hasContext() && CONTEXT.get().isActive();
  }

  public static void rollback() {
    getContext().setRollbackOnly();
  }

  public static <V> V rollback(final Throwable e) {
    getContext().setRollbackOnly(e);
    return null;
  }

  protected static <C extends TransactionContext> Runnable runnable(final C context,
    final RunAction action) {
    return () -> {
      try {
        action.run();
      } catch (final Throwable t) {
        context.setRollbackOnly(t);
      }
    };
  }

  private static BaseCloseable suspend() {
    final TransactionContext context = getContext();
    if (context instanceof final ActiveTransactionContext mapContext) {
      return mapContext.suspend();
    } else {
      return BaseCloseable.EMPTY;
    }
  }

  public static TransactionBuilder transaction() {
    return TransactionBuilder.BUILDER;
  }

  static Carrier where(final TransactionContext context) {
    return ScopedValue.where(CONTEXT, context);
  }

}
