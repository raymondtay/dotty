package dotty
package tools
package vulpix

object TestConfiguration {

  val noCheckOptions = Array(
    "-pagewidth", "120",
    "-color:never"
  )

  val checkOptions = Array(
    // "-Yscala2-unpickler", s"${Jars.scalaLibrary}:${Jars.scalaXml}",
    "-Yno-deep-subtypes",
    "-Yno-double-bindings",
    "-Yforce-sbt-phases",
    "-Xverify-signatures"
  )

  val classPath = mkClassPath(Jars.dottyTestDeps)
  val runClassPath = mkClassPath(Jars.dottyLib :: Nil)

  def mkClassPath(classPaths: List[String]): String = {
    classPaths map { p =>
      val file = new java.io.File(p)
      assert(
        file.exists,
        s"""|File "$p" couldn't be found. Run `packageAll` from build tool before
            |testing.
            |
            |If running without sbt, test paths need to be setup environment variables:
            |
            | - DOTTY_LIBRARY
            | - DOTTY_COMPILER
            | - DOTTY_INTERFACES
            | - DOTTY_EXTRAS
            |
            |Where these all contain locations, except extras which is a colon
            |separated list of jars.
            |
            |When compiling with eclipse, you need the sbt-interfaces jar, put
            |it in extras."""
      )
      file.getAbsolutePath
    } mkString(":")
  }

  // Ideally should be Ycheck:all
  val yCheckOptions = Array("-Ycheck:all")

  val basicDefaultOptions = checkOptions ++ noCheckOptions ++ yCheckOptions
  val defaultOptions = TestFlags(classPath, runClassPath, basicDefaultOptions)
  val defaultRunWithCompilerOptions = defaultOptions.withRunClasspath(Jars.dottyRunWithCompiler.mkString(":"))
  val allowDeepSubtypes = defaultOptions without "-Yno-deep-subtypes"
  val allowDoubleBindings = defaultOptions without "-Yno-double-bindings"
  val picklingOptions = defaultOptions and (
    "-Xprint-types",
    "-Ytest-pickler",
    "-Yprint-pos",
    "-Yprint-pos-syms"
  )
  val scala2Mode = defaultOptions and "-language:Scala2"
  val explicitUTF8 = defaultOptions and ("-encoding", "UTF8")
  val explicitUTF16 = defaultOptions and ("-encoding", "UTF16")
}
