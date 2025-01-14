package com.revolsys.gis.postgresql.type;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.dbcp2.DelegatingConnection;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import com.revolsys.exception.Exceptions;
import com.revolsys.jdbc.field.JdbcBlobFieldDefinition;
import com.revolsys.spring.resource.Resource;

public class PostgreSQLJdbcBlobFieldDefinition extends JdbcBlobFieldDefinition {

  public PostgreSQLJdbcBlobFieldDefinition(final String dbName, final String name,
    final int sqlType, final String dbDataType, final int length, final int scale,
    final boolean required, final String description, final Map<String, Object> properties) {
    super(dbName, name, sqlType, dbDataType, length, required, description, properties);
  }

  @Override
  public PostgreSQLJdbcBlobFieldDefinition clone() {
    final PostgreSQLJdbcBlobFieldDefinition clone = new PostgreSQLJdbcBlobFieldDefinition(
      getDbName(), getName(), getSqlType(), getDbDataType(), getLength(), getScale(), isRequired(),
      getDescription(), getProperties());
    postClone(clone);
    return clone;
  }

  private InputStream openInputStream(final Object value) {
    if (value instanceof InputStream) {
      return (InputStream)value;
    } else if (value instanceof byte[]) {
      final byte[] bytes = (byte[])value;
      return new ByteArrayInputStream(bytes);
    } else if (value instanceof CharSequence) {
      final String string = ((CharSequence)value).toString();
      final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
      return new ByteArrayInputStream(bytes);
    } else {
      try {
        final Resource resource = Resource.getResource(value);
        return resource.newBufferedInputStream();
      } catch (final IllegalArgumentException e) {
        throw new IllegalArgumentException(value.getClass() + " not valid for a blob column");
      }
    }
  }

  @Override
  public int setPreparedStatementValue(final PreparedStatement statement, final int parameterIndex,
    final Object value) throws SQLException {
    if (value == null) {
      final int sqlType = getSqlType();
      statement.setNull(parameterIndex, sqlType);
    } else {
      Blob blob;
      if (value instanceof Blob) {
        blob = (Blob)value;
        statement.setBlob(parameterIndex, blob);
      } else {
        try (
          InputStream in = openInputStream(value)) {
          final PGConnection pgConnection = (PGConnection)((DelegatingConnection<?>)statement
            .getConnection()).getInnermostDelegate();
          final LargeObjectManager lobManager = pgConnection.getLargeObjectAPI();

          final long lobId = lobManager
            .createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

          final LargeObject lob = lobManager.open(lobId, LargeObjectManager.WRITE);
          try {
            final byte buffer[] = new byte[2048];
            int readCount = 0;
            while ((readCount = in.read(buffer, 0, 2048)) > 0) {
              lob.write(buffer, 0, readCount);
            }
          } finally {
            lob.close();
          }
          statement.setLong(parameterIndex, lobId);
        } catch (final IOException e) {
          Exceptions.throwUncheckedException(e);
        }
      }
    }
    return parameterIndex + 1;
  }
}
