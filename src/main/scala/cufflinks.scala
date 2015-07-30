package ohnosequencesBundles.statika

import ohnosequences.statika._, bundles._, instructions._
import java.io.File


abstract class Cufflinks(val version: String) extends Bundle() {

  val usrbin = "/usr/bin/"
  val cufflinksDistribution = s"cufflinks-${version}.Linux_x86_64"

  val commands: Set[String] = Set(
    "cuffcompare",
    "cuffdiff",
    "cufflinks",
    "cuffmerge",
    "cuffnorm",
    "cuffquant",
    "gffread",
    "gtf_to_sam"
  )

  def linkCommand (cmd: String) : Results = Seq("ln", "-s", new File(s"${cufflinksDistribution}/${cmd}").getAbsolutePath,  s"/usr/bin/${cmd}")


  def install: Results = {
    Seq("wget", s"http://s3-eu-west-1.amazonaws.com/resources.ohnosequences.com/cufflinks/${version}/${cufflinksDistribution}.tar.gz", "-O", s"${cufflinksDistribution}.tar.gz") ->-
    Seq("tar", "-xvf", s"${cufflinksDistribution}.tar.gz") ->-
    commands.foldLeft[Results](
      Seq("echo", "linking cufflinks binaries")
    ){(acc, cmd) => acc ->- linkCommand(cmd)} ->-
    success(s"${bundleName} is installed")
  }

}
