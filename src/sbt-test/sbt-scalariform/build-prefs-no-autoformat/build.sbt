import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

name := "test"
version := "0.1"

scalariformAutoformat := false

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignArguments, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 20)
  .setPreference(CompactControlReadability, true)
  .setPreference(DanglingCloseParenthesis, Preserve)
  .setPreference(SpacesAroundMultiImports, false)

