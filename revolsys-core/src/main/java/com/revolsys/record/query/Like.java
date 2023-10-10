package com.revolsys.record.query;

import org.jeometry.common.collection.map.MapEx;
import org.jeometry.common.data.type.DataType;

import com.revolsys.util.Property;

public class Like extends BinaryCondition {

  public static String toPattern(String value2) {
    value2 = value2.replaceAll("\\\\", "\\\\");
    value2 = value2.replaceAll("\\[", "\\[");
    value2 = value2.replaceAll("\\]", "\\]");
    value2 = value2.replaceAll("\\{", "\\{");
    value2 = value2.replaceAll("\\}", "\\}");
    value2 = value2.replaceAll("\\(", "\\)");
    value2 = value2.replaceAll("\\)", "\\)");
    value2 = value2.replaceAll("\\^", "\\^");
    value2 = value2.replaceAll("\\$", "\\$");
    value2 = value2.replaceAll("\\+", "\\+");
    value2 = value2.replaceAll("\\-", "\\-");
    value2 = value2.replaceAll("\\*", "\\*");
    value2 = value2.replaceAll("\\?", "\\?");
    value2 = value2.replaceAll("\\|", "\\|");
    value2 = value2.replaceAll("\\,", "\\,");
    value2 = value2.replaceAll("\\:", "\\:");
    value2 = value2.replaceAll("\\!", "\\!");
    value2 = value2.replaceAll("\\<", "\\<");
    value2 = value2.replaceAll("\\>", "\\>");
    value2 = value2.replaceAll("\\=", "\\=");
    value2 = value2.replaceAll("%", ".*");
    return value2;
  }

  public Like(final QueryValue left, final QueryValue right) {
    super(left, "LIKE", right);
  }

  @Override
  public Like clone() {
    return (Like)super.clone();
  }

  @Override
  public Like newCondition(final QueryValue left, final QueryValue right) {
    return new Like(left, right);
  }

  @Override
  public boolean test(final MapEx record) {
    final QueryValue left = getLeft();
    final String value1 = left.getStringValue(record);

    final QueryValue right = getRight();
    String value2 = right.getStringValue(record);

    if (org.jeometry.common.util.Property.hasValue(value1)) {
      if (org.jeometry.common.util.Property.hasValue(value2)) {
        if (value2.contains("%")) {
          value2 = toPattern(value2);
          if (value1.matches(value2)) {
            return true;
          } else {
            return false;
          }
        } else {
          return DataType.equal(value1, value2);
        }
      } else {
        return false;
      }
    } else {
      return !org.jeometry.common.util.Property.hasValue(value2);
    }
  }

}
