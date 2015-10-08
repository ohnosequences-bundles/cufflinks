package ohnosequencesBundles.statika

import ohnosequences.statika._, bundles._, instructions._
import java.io.File


abstract class Cufflinks(val version: String) extends Bundle() { cufflinks =>

  val name = s"cufflinks-${version}.Linux_x86_64"
  val tarGz = name + ".tar.gz"

  val binaries: Set[String] = Set(
    "cuffcompare",
    "cuffdiff",
    "cufflinks",
    "cuffmerge",
    "cuffnorm",
    "cuffquant",
    "gffread",
    "gtf_to_sam"
  )

  lazy val download: CmdInstructions = cmd("wget")(
    s"http://s3-eu-west-1.amazonaws.com/resources.ohnosequences.com/cufflinks/${version}/${cufflinks.tarGz}"
  )

  lazy val untar: CmdInstructions = cmd("tar")("-xvzf", cufflinks.tarGz)

  def linkCommand(binary: String): CmdInstructions = cmd("ln")("-s",
    new File(name, binary).getCanonicalPath,
    s"/usr/bin/${binary}"
  )

  def instructions: AnyInstructions =
    download -&-
    untar -&-
    binaries.foldLeft[AnyInstructions]( Seq("echo", "linking binaries") ){ _ -&- linkCommand(_) }

}
