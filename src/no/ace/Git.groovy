#!/usr/bin/env groovy
package no.ace

import java.util.regex.Matcher

class Git {
  static Boolean isMasterBranch(Map env) {
    return env.BRANCH_NAME == 'master'
  }

  static Boolean isDevelopBranch(Map env) {
    return env.BRANCH_NAME == 'develop'
  }

  static Boolean isFeatureBranch(Map env) {
    return (env.BRANCH_NAME =~ /^feature\//) != false
  }

  static Boolean isReleaseBranch(Map env) {
    return (env.BRANCH_NAME =~ /^release\/(v[0-9]+\.[0-9]+\.[0-9]+)/) != false
  }

  static String releaseBranchVersion(Map env) {
    Matcher m = env.BRANCH_NAME =~ /^release\/(v[0-9]+\.[0-9]+\.[0-9]+)/
    return m ? m.group(1) : ''
  }

  static Boolean isPR(Map env) {
    return env.containsKey('CHANGE_ID')
  }

  static String prId(Map env) {
    return env.CHANGE_ID ?: ''
  }

  static String prUrl(Map env) {
    return env.CHANGE_URL ?: ''
  }

  static String findLatestTag(String command) {
    int exitValue
    String sout
    String serr
    (exitValue,  sout, serr) = runCommand('git tag --points-at HEAD')

    if (exitValue != 0) {
      System.err.println("Error looking up git-tags: " + serr)
      return null
    } else {
      def parts = sout.split('\r?\n')
      return parts[parts.length-1]
    }
  }

  static List runCommand(String command) {
    def sout = new StringBuilder(), serr = new StringBuilder();
    def proc = command.execute();
    proc.consumeProcessOutput(sout, serr);
    proc.waitForOrKill(1000)
    return [proc.exitValue(), sout, serr]
  }
}

