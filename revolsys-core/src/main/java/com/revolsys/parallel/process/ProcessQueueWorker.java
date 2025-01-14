package com.revolsys.parallel.process;

import com.revolsys.exception.WrappedInterruptedException;
import com.revolsys.logging.Logs;
import com.revolsys.parallel.channel.Channel;
import com.revolsys.parallel.channel.ClosedException;

public class ProcessQueueWorker extends Thread {
  private final Channel<Process> in;

  private Process process;

  private final ProcessQueue queue;

  public ProcessQueueWorker(final ProcessQueue queue) {
    this.queue = queue;
    this.in = queue.getProcessChannel();
    setDaemon(true);
  }

  public String getBeanName() {
    return getClass().getName();
  }

  public Process getProcess() {
    return this.process;
  }

  @Override
  public void run() {
    this.queue.addWorker(this);
    try {
      while (true) {
        this.process = this.in.read(this.queue.getMaxWorkerIdleTime());
        if (this.process == null) {
          return;
        } else {
          try {
            this.process.run();
          } catch (final WrappedInterruptedException e) {
            return;
          } catch (final Exception e) {
            Logs.error(this.process, e.getMessage(), e);
          }
        }
        this.process = null;
      }
    } catch (final ClosedException e) {
      return;
    } finally {
      this.queue.removeWorker(this);
    }
  }

  public void setBeanName(final String name) {
  }
}
