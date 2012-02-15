package com.revolsys.ui.web.controller;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.PathMatcher;

import com.revolsys.util.CompareUtil;

public class UriTemplatePathMatcher implements PathMatcher {

  public String combine(final String arg0, final String arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  public String extractPathWithinPattern(final String pattern, final String path) {
    if (matchStart(pattern, path)) {
      return path;
    } else {
      return "";
    }
  }

  public Map<String, String> extractUriTemplateVariables(
    final String pattern,
    final String path) {
    final Map<String, String> variables = new LinkedHashMap<String, String>();
    return variables;
  }

  private Matcher getMatcher(final String pattern, final String path) {
    String regex = pattern;
    // regex = regex.replaceAll("\\*\\*", "(.*)");
    regex = regex.replaceAll("\\*", "([^/]+)");
    regex = regex.replaceAll("\\(\\[\\^/\\]\\+\\)\\(\\[\\^/\\]\\+\\)", "(.*)");
    regex = regex.replaceAll("\\?", "(.?)");
    regex = regex.replaceAll("\\{[^\\}]+\\}", "([^/]+)");
    final Pattern rePattern = Pattern.compile(regex);
    final Matcher matcher = rePattern.matcher(path);
    return matcher;
  }

  public Comparator<String> getPatternComparator(final String arg0) {
    return new Comparator<String>() {
      public int compare(final String pattern1, final String Pattern2) {
        // TODO improve
        return -CompareUtil.compare(pattern1.length(), Pattern2.length());
      }
    };
  }

  public boolean isPattern(final String path) {
    return path.contains("{") || path.contains("*") || path.contains("?");
  }

  public boolean match(final String pattern, final String path) {
    final Matcher matcher = getMatcher(pattern, path);
    return matcher.matches();
  }

  public boolean matchStart(final String pattern, final String path) {
    final Matcher matcher = getMatcher(pattern, path);
    if (matcher.find()) {
      return matcher.start() == 0;
    } else {
      return false;
    }
  }

}
