/*
 *  Copyright 2022 Thomas (Tom.bat) O'Sullivan
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at:
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package dev.tomdotbat.dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Environment File of the application.
 */
/* package-private */ final class EnvironmentFile {
    /**
     * Creates an Environment File from the given File.
     * @param file the Environment File.
     */
    public EnvironmentFile(File file) {
        this.FILE = file;
    }

    /**
     * Creates an Environment File from the File at the given Path.
     * @param path the path of the Environment File.
     */
    public EnvironmentFile(Path path) {
        this(new File(path.toString()));
    }

    /**
     * Loads the settings from the Environment File.
     * @return a HashMap containing the File's key-value paired settings.
     * @throws IOException if an error occurs when reading the File.
     */
    public Map<String, String> load() throws IOException {
        final Map<String, String> SETTINGS = new HashMap<>();

        //Read each setting line-by-line, stripping declarator characters if any.
        for (final String LINE: spiltTextByLines(Files.readString(FILE.toPath()))) {
            final String[] KV_PAIR = stripComment(LINE).split(KV_SEPARATOR, 2);
            if (KV_PAIR.length < 2) continue;

            SETTINGS.put(KV_PAIR[0].trim().toUpperCase(), extractValue(KV_PAIR[1]));
        }

        return SETTINGS;
    }

    /**
     * Saves the settings to the Environment File.
     * @param SETTINGS the settings as a HashMap.
     * @throws IOException if an error occurs when writing to the File.
     */
    public void save(final Map<String, String> SETTINGS) throws IOException {
        if (!FILE.canWrite())
            throw new IOException("Insufficient permission to write to the Environment File.");

        final Map<String, String> SETTINGS_TO_SAVE = new HashMap<>(SETTINGS);
        final StringBuilder FILE_TEXT_BUILDER = new StringBuilder();

        final String NEW_LINE = "\n";
        final String SPACE = " ";

        //Replace the existing settings in the File with their new values.
        for (final String LINE: spiltTextByLines(Files.readString(FILE.toPath()))) {
            final String[] KV_PAIR = stripComment(LINE).split(KV_SEPARATOR, 2);
            if (KV_PAIR.length < 2) {
                FILE_TEXT_BUILDER.append(LINE).append(NEW_LINE);
                continue;
            }

            final String KEY = KV_PAIR[0];
            if (!SETTINGS_TO_SAVE.containsKey(KEY)) continue;

            FILE_TEXT_BUILDER.append(KEY).append(KV_SEPARATOR).append(SETTINGS_TO_SAVE.get(KEY));

            final String COMMENT = getComment(LINE);
            if (COMMENT == null) FILE_TEXT_BUILDER.append(NEW_LINE);
            else FILE_TEXT_BUILDER.append(SPACE).append(COMMENT_DECLARATOR).append(COMMENT);

            SETTINGS_TO_SAVE.remove(KEY);
        }

        //Add any new settings to the end of the file.
        if (SETTINGS_TO_SAVE.size() > 0) {
            FILE_TEXT_BUILDER.append(NEW_LINE);

            for (final Map.Entry<String, String> ENTRY: SETTINGS_TO_SAVE.entrySet())
                FILE_TEXT_BUILDER.append(ENTRY.getKey()).append(KV_SEPARATOR).append(ENTRY.getValue()).append(NEW_LINE);
        }

        Files.writeString(FILE.toPath(), FILE_TEXT_BUILDER.toString());
    }

    /**
     * Splits a String into an array of each line it contains.
     * @param TEXT the String to split.
     * @return an array of lines in the String.
     */
    private String[] spiltTextByLines(final String TEXT) {
        return TEXT.replaceAll("\r", "").split("\n");
    }

    /**
     * Removes the comment from the line of text provided if one exists.
     * @param TEXT the line of text to strip the comment from.
     * @return the line of text with the comment removed.
     */
    private String stripComment(final String TEXT) {
        final String[] SPLIT_TEXT = TEXT.split(COMMENT_DECLARATOR);

        if (SPLIT_TEXT.length < 2)
            return TEXT;

        if (SPLIT_TEXT.length == 2)
            return SPLIT_TEXT[0];

        return Arrays.toString(Arrays.copyOfRange(SPLIT_TEXT, 0, SPLIT_TEXT.length - 2));
    }

    /**
     * Gets the comment from a line of text if it exists.
     * @param TEXT the text to get the comment from.
     * @return the comment in the text, null if there isn't one.
     */
    private String getComment(final String TEXT) {
        final String[] SPLIT_TEXT = TEXT.split(COMMENT_DECLARATOR);
        return SPLIT_TEXT.length < 2 ? null : SPLIT_TEXT[SPLIT_TEXT.length - 1];
    }

    /**
     * Gets the text between a pair of given characters.
     * @param TEXT the text to extract from.
     * @param CHAR the character to use as the pair around the text.
     * @return the text between the char pair, null if the chars aren't present.
     */
    private String getTextBetweenCharPair(final String TEXT, final char CHAR) {
        if (TEXT.charAt(0) != CHAR)
            return null;

        if (TEXT.charAt(TEXT.length() - 1) != CHAR)
            return null;

        return TEXT.substring(1, TEXT.length() - 1);
    }

    /**
     * Extracts the value from a String of text.
     * @param TEXT the text to extract from.
     * @return the extracted value.
     */
    private String extractValue(final String TEXT) {
        final String TRIMMED_TEXT = TEXT.trim();

        for (final char STRING_DECLARATOR: STRING_DECLARATION_CHARS) {
            String value = getTextBetweenCharPair(TRIMMED_TEXT, STRING_DECLARATOR);
            if (value != null) return value;
        }

        return TRIMMED_TEXT;
    }

    private final File FILE;
    private final String KV_SEPARATOR = "=";
    private final String COMMENT_DECLARATOR = "#";
    private final char[] STRING_DECLARATION_CHARS = new char[]{'"', '\''};
}
