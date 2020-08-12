import jdk.nashorn.internal.AssertsEnabled
@Grab('org.yaml:snakeyaml:1.23')
import org.yaml.snakeyaml.Yaml

import no.ace.Config
import no.ace.Git

class GitTest extends GroovyTestCase {
  String fixtures = 'test/fixtures'

  void testGitTag() {
    try {
      Git.runCommand("git tag 0.01-test")
      Git.runCommand("git tag 0.02-test")
      def tag = Git.findLatestTag()
      assertEquals("0.02-test", tag)
    }
    finally {
      Git.runCommand("git tag -d 0.01-test")
      Git.runCommand("git tag -d 0.02-test")
    }
  }


}
