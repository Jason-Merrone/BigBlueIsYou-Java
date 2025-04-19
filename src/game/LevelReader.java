package game;

import ecs.Entities.Entity;
import ecs.Entities.Noun;
import ecs.Entities.Object;
import ecs.Entities.Verb;
import org.joml.Vector3i;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Refactored LevelReader:
 *   - initializeLevels() → scan all files in resources/levels/*
 *   - getLevelDescriptors() → list of available levels
 *   - loadLevel(...) → load exactly one level by descriptor
 */
public class LevelReader {
    /**
     * Metadata for a single level within a file.
     */
    public static class LevelDescriptor {
        private final String filePath;
        private final int    index;   // zero‐based in that file
        private final String name;
        private final int    width;
        private final int    height;

        public LevelDescriptor(String filePath, int index, String name, int width, int height) {
            this.filePath = filePath;
            this.index    = index;
            this.name     = name;
            this.width    = width;
            this.height   = height;
        }
        public String getFilePath() { return filePath; }
        public int    getIndex()    { return index;    }
        public String getName()     { return name;     }
        public int    getWidth()    { return width;    }
        public int    getHeight()   { return height;   }
        @Override public String toString() {
            return String.format("[%d] %s (%dx%d) in %s",
                    index, name, width, height, filePath);
        }
    }

    private final List<LevelDescriptor> descriptors = new ArrayList<>();
    private LevelDescriptor currentDescriptor;

    public void setLevelDescriptor(LevelDescriptor descriptor) {
        currentDescriptor = descriptor;
    }
    public LevelDescriptor getCurrentDescriptor() {
        return currentDescriptor;
    }

    /**
     * Scan every file under resources/levels and build our list of LevelDescriptor.
     * Catches its own IOException so callers don’t need to.
     */
    public void initializeLevels() {
        descriptors.clear();
        Path levelsDir = Paths.get("resources/levels");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(levelsDir)) {
            for (Path p : stream) {
                if (Files.isRegularFile(p)) {
                    parseFileForDescriptors(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Error scanning levels directory: " + e.getMessage());
        }
    }

    /**
     * Reads one file’s contents to discover each level header & dimensions.
     * Catches its own IOException so that initializeLevels() can keep going.
     */
    private void parseFileForDescriptors(Path path) {
        try (BufferedReader r = Files.newBufferedReader(path)) {
            String titleLine;
            int idx = 0;
            while ((titleLine = r.readLine()) != null) {
                if (titleLine.trim().isEmpty()) continue;
                String levelName = titleLine.trim();

                String sizeLine = r.readLine();
                if (sizeLine == null) break;
                String[] parts = sizeLine.toLowerCase().split("x");
                int w = Integer.parseInt(parts[0].trim());
                int h = Integer.parseInt(parts[1].trim());

                // record this level’s metadata
                descriptors.add(new LevelDescriptor(
                        path.toString(), idx++, levelName, w, h));

                // skip the two h‑row groups
                for (int i = 0; i < h * 2; i++) {
                    r.readLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading level file " + path + ": " + e.getMessage());
        }
    }

    /**
     * @return an unmodifiable list of all discovered levels
     */
    public List<LevelDescriptor> getLevelDescriptors() {
        return Collections.unmodifiableList(descriptors);
    }

    /**
     * Load exactly one level by descriptor, creating entities only.
     * Already catches I/O errors internally.
     */
    public List<Entity> loadLevelEntities(LevelDescriptor desc) {
        String filePath = desc.getFilePath();
        int    target   = desc.getIndex();
        int    width    = desc.getWidth();
        int    height   = desc.getHeight();
        List<Entity> entities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int    current = -1;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                current++;
                reader.readLine();  // skip size line

                if (current != target) {
                    // skip both layers
                    for (int i = 0; i < height * 2; i++) {
                        reader.readLine();
                    }
                    continue;
                }

                // target level — background layer
                for (int row = 0; row < height; row++) {
                    String bg = reader.readLine();
                    for (int col = 0; col < width; col++) {
                        char c = col < bg.length() ? bg.charAt(col) : ' ';
                        createEntity(desc.getName(), c, row, col, entities);
                    }
                }
                // object layer
                for (int row = 0; row < height; row++) {
                    String fg = reader.readLine();
                    for (int col = 0; col < width; col++) {
                        char c = col < fg.length() ? fg.charAt(col) : ' ';
                        createEntity(desc.getName(), c, row, col, entities);
                    }
                }
                return entities;
            }

        } catch (FileNotFoundException fnf) {
            System.err.println("Cannot open level file: " + filePath);
        } catch (IOException ioe) {
            System.err.println("Error reading level " + target + ": " + ioe.getMessage());
        }

        return Collections.emptyList();
    }

    void createEntity(String name, Character objectChar, int row, int col, List<Entity> entities) {
        if (objectChar == null) return;
        if (objectChar == ' ') return;

        System.out.println("objectChar: " + objectChar + " row: " + row + " col: " + col);

        Vector3i position = new Vector3i();
        position.x = col;
        position.y = row;
        if (Character.isUpperCase(objectChar)) {
            position.z = 2;
            Noun.NounType nounType = null;
            if (objectChar == 'I') {
                Verb.VerbType verbType = Verb.VerbType.IS;
                entities.add(Verb.create(verbType, position)); // TODO: Figure out how to actually add these
                return;
            }
            else if (objectChar == 'W') { nounType = Noun.NounType.WALL; }
            else if (objectChar == 'R') { nounType = Noun.NounType.ROCK; }
            else if (objectChar == 'F') { nounType = Noun.NounType.FLAG; }
            else if (objectChar == 'B') { nounType = Noun.NounType.BIGBLUE; }
            else if (objectChar == 'S') { nounType = Noun.NounType.STOP; }
            else if (objectChar == 'P') { nounType = Noun.NounType.PUSH; }
            else if (objectChar == 'V') { nounType = Noun.NounType.LAVA; }
            else if (objectChar == 'A') { nounType = Noun.NounType.WATER; }
            else if (objectChar == 'Y') { nounType = Noun.NounType.YOU; }
            else if (objectChar == 'X') { nounType = Noun.NounType.WIN; }
            else if (objectChar == 'N') { nounType = Noun.NounType.SINK; }
            else if (objectChar == 'K') { nounType = Noun.NounType.KILL; }
            else {
                throw new IllegalArgumentException("Unrecognized object character: " + objectChar);
            }
            entities.add(Noun.create(nounType, position)); // TODO: Figure out how to actually add these
        } else {
            position.z = 2;
            Object.ObjectType objectType = null;
            if (objectChar == 'w') { objectType = Object.ObjectType.WALL; }
            else if (objectChar == 'r') { objectType = Object.ObjectType.ROCK; }
            else if (objectChar == 'f') { objectType = Object.ObjectType.FLAG; }
            else if (objectChar == 'b') { objectType = Object.ObjectType.BIGBLUE; }
            else if (objectChar == 'l') { objectType = Object.ObjectType.FLOOR;  position.z = 1; }
            else if (objectChar == 'g') { objectType = Object.ObjectType.GRASS;  position.z = 1; }
            else if (objectChar == 'a') { objectType = Object.ObjectType.WATER;  position.z = 1; }
            else if (objectChar == 'v') { objectType = Object.ObjectType.LAVA;  position.z = 1; }
            else if (objectChar == 'h') { objectType = Object.ObjectType.HEDGE; }
            else {
                throw new IllegalArgumentException("Unrecognized object character: " + objectChar);
            }
            entities.add(Object.create(objectType, position)); // TODO: Figure out how to actually add these
        }

    }
}