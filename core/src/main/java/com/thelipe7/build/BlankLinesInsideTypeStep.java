/*
 * Copyright 2026 thelipe7
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thelipe7.build;

import com.diffplug.spotless.FormatterFunc;
import com.diffplug.spotless.FormatterStep;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.io.Serializable;

public final class BlankLinesInsideTypeStep {

    private static final String NAME = "blank-lines-inside-types";

    private BlankLinesInsideTypeStep() {
    }

    public static FormatterStep create() {
        return FormatterStep.create(NAME, new State(), State::toFormatter);
    }

    private static final class State implements Serializable {

        private static final long serialVersionUID = 1L;

        private FormatterFunc toFormatter() {
            return State::format;
        }

        private static String format(String string) {
            List<TypeRange> typeRanges = findTypeRanges(string);
            if (typeRanges.isEmpty()) {
                return string;
            }

            List<String> lines = new ArrayList<>(Arrays.asList(string.split("\n", -1)));
            typeRanges.sort(Comparator.comparingInt(TypeRange::openLine).reversed());

            for (TypeRange typeRange : typeRanges) {
                applyBlankLines(lines, typeRange);
            }

            return String.join("\n", lines);
        }

        private static List<TypeRange> findTypeRanges(String string) {
            List<TypeRange> typeRanges = new ArrayList<>();
            List<String> recentTokens = new ArrayList<>();
            Deque<Boolean> typeBraceStack = new ArrayDeque<>();
            Deque<TypeStart> pendingTypes = new ArrayDeque<>();

            int line = 0;
            int column = 0;
            int index = 0;

            while (index < string.length()) {
                char character = string.charAt(index);

                if (character == '\n') {
                    line++;
                    column = 0;
                    index++;
                    continue;
                }

                if (Character.isWhitespace(character)) {
                    column++;
                    index++;
                    continue;
                }

                if (character == '/' && index + 1 < string.length()) {
                    char next = string.charAt(index + 1);

                    if (next == '/') {
                        while (index < string.length() && string.charAt(index) != '\n') {
                            index++;
                            column++;
                        }
                        continue;
                    }

                    if (next == '*') {
                        index += 2;
                        column += 2;
                        while (index < string.length() - 1) {
                            if (string.charAt(index) == '\n') {
                                line++;
                                column = 0;
                                index++;
                                continue;
                            }
                            if (string.charAt(index) == '*' && string.charAt(index + 1) == '/') {
                                index += 2;
                                column += 2;
                                break;
                            }
                            index++;
                            column++;
                        }
                        continue;
                    }
                }

                if (character == '"') {
                    if (index + 2 < string.length()
                            && string.charAt(index + 1) == '"'
                            && string.charAt(index + 2) == '"') {
                        index += 3;
                        column += 3;
                        while (index < string.length() - 2) {
                            if (string.charAt(index) == '\n') {
                                line++;
                                column = 0;
                                index++;
                                continue;
                            }
                            if (string.charAt(index) == '"'
                                    && string.charAt(index + 1) == '"'
                                    && string.charAt(index + 2) == '"') {
                                index += 3;
                                column += 3;
                                break;
                            }
                            index++;
                            column++;
                        }
                        continue;
                    }

                    index++;
                    column++;
                    while (index < string.length()) {
                        char current = string.charAt(index);
                        if (current == '\\') {
                            index += 2;
                            column += 2;
                            continue;
                        }
                        if (current == '\n') {
                            line++;
                            column = 0;
                            index++;
                            continue;
                        }
                        index++;
                        column++;
                        if (current == '"') {
                            break;
                        }
                    }
                    continue;
                }

                if (character == '\'') {
                    index++;
                    column++;
                    while (index < string.length()) {
                        char current = string.charAt(index);
                        if (current == '\\') {
                            index += 2;
                            column += 2;
                            continue;
                        }
                        if (current == '\n') {
                            line++;
                            column = 0;
                            index++;
                            continue;
                        }
                        index++;
                        column++;
                        if (current == '\'') {
                            break;
                        }
                    }
                    continue;
                }

                if (Character.isJavaIdentifierStart(character)) {
                    int start = index;
                    while (index < string.length() && Character.isJavaIdentifierPart(string.charAt(index))) {
                        index++;
                        column++;
                    }
                    recentTokens.add(string.substring(start, index));
                    continue;
                }

                if (character == '@') {
                    recentTokens.add("@");
                    index++;
                    column++;
                    continue;
                }

                if (character == ';') {
                    recentTokens.clear();
                    index++;
                    column++;
                    continue;
                }

                if (character == '{') {
                    boolean isType = looksLikeTypeDeclaration(recentTokens);
                    typeBraceStack.push(isType);
                    if (isType) {
                        pendingTypes.push(new TypeStart(line, column));
                    }
                    recentTokens.clear();
                    index++;
                    column++;
                    continue;
                }

                if (character == '}') {
                    if (!typeBraceStack.isEmpty() && typeBraceStack.pop()) {
                        TypeStart typeStart = pendingTypes.pop();
                        typeRanges.add(new TypeRange(typeStart.line(), typeStart.column(), line, column));
                    }
                    recentTokens.clear();
                    index++;
                    column++;
                    continue;
                }

                index++;
                column++;
            }

            return typeRanges;
        }

        private static boolean looksLikeTypeDeclaration(List<String> recentTokens) {
            for (int index = 0; index < recentTokens.size(); index++) {
                String token = recentTokens.get(index);
                if ("class".equals(token) || "enum".equals(token) || "record".equals(token)) {
                    return true;
                }
                if ("interface".equals(token)) {
                    return true;
                }
            }
            return false;
        }

        private static void applyBlankLines(List<String> lines, TypeRange typeRange) {
            if (typeRange.openLine() == typeRange.closeLine()) {
                expandSingleLineType(lines, typeRange);
                return;
            }

            int closeLine = typeRange.closeLine();
            if (typeRange.openLine() + 1 < lines.size() && !lines.get(typeRange.openLine() + 1).isBlank()) {
                lines.add(typeRange.openLine() + 1, "");
                closeLine++;
            }

            if (closeLine - 1 >= 0 && !lines.get(closeLine - 1).isBlank()) {
                lines.add(closeLine, "");
            }
        }

        private static void expandSingleLineType(List<String> lines, TypeRange typeRange) {
            String line = lines.get(typeRange.openLine());
            String indentation = leadingWhitespace(line);
            String beforeOpeningBrace = line.substring(0, typeRange.openColumn() + 1);
            String closingAndAfter = line.substring(typeRange.closeColumn() + 1).trim();

            lines.set(typeRange.openLine(), beforeOpeningBrace);
            lines.add(typeRange.openLine() + 1, "");
            lines.add(typeRange.openLine() + 2, indentation + closingAndAfter);
        }

        private static String leadingWhitespace(String line) {
            int index = 0;
            while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
                index++;
            }
            return line.substring(0, index);
        }

    }

    private static record TypeStart(int line, int column) {

    }

    private static record TypeRange(int openLine, int openColumn, int closeLine, int closeColumn) {

    }

}
