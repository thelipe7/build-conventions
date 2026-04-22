import com.thelipe7.build.BlankLinesInsideTypeStep

plugins {
    id("com.diffplug.spotless")
}

spotless {
    java {
        target("src/*/java/**/*.java")

        cleanthat()
            .addMutator("SafeAndConsensual")
            .addMutator("SafeButControversial")
            .addMutator("SafeButNotConsensual")
            .excludeMutator("AvoidInlineConditionals")
            .excludeMutator("AvoidUncheckedExceptionsInSignatures")
            .excludeMutator("LiteralsFirstInComparisons")
            .excludeMutator("LocalVariableTypeInference")
            .addMutator("AppendCharacterWithChar")
            .addMutator("ForEachIfBreakElseToStreamTakeWhile")
            .addMutator("ForEachIfBreakToStreamFindFirst")
            .addMutator("GuavaImmutableMapBuilderOverVarargs")
            .addMutator("ImportQualifiedTokens")
            .addMutator("NullCheckToOptionalOfNullable")
            .addMutator("OptionalMapIdentity")
            .addMutator("OptionalWrappedIfToFilter")
            .addMutator("StreamFlatMapStreamToFlatMap")
            .addMutator("StreamForEachNestingForLoopToFlatMap")
            .addMutator("StreamMapIdentity")
            .addMutator("StreamWrappedMethodRefToMap")
            .addMutator("StringFromString")
            .addMutator("UnnecessaryCaseChange")
            .addMutator("UseDiamondOperator")
            .addMutator("UseDiamondOperatorJdk8")
            .addMutator("UsePredefinedStandardCharset")

        forbidWildcardImports()
        palantirJavaFormat()
        addStep(BlankLinesInsideTypeStep.create())
        formatAnnotations()
        licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }

    yaml {
        target("src/**/*.yml", "src/**/*.yaml")
        prettier()
        trimTrailingWhitespace()
        endWithNewline()
    }

    format("misc") {
        target("*.md", ".gitignore", ".editorconfig")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
