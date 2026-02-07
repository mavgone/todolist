import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class MainTest {

    private ArrayList<Task> tasks;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
    }

    @Test
    void test_addTask() {
        Task task1 = new Task("адин", false, 1);
        tasks.add(task1);

        Task task2 = new Task("два", false, 2);
        tasks.add(task2);

        assertEquals(2, tasks.size());
        assertEquals(2, tasks.get(1).getId());
    }

    @Test
    void test_deleteTask() {
        tasks.add(new Task("адин", false, 1));
        tasks.add(new Task("два", false, 2));
        tasks.add(new Task("трии", false, 3));

        tasks.remove(1);

        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setId(i + 1);
        }

        assertEquals(1, tasks.get(0).getId());
        assertEquals(2, tasks.get(1).getId());
    }

    @Test
    void test_saveTask() {
        tasks.add(new Task("сэйв", true, 1));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class);
             MockedStatic<Path> mockedPath = mockStatic(Path.class)) {

            Path mockPath = mock(Path.class);
            when(Path.of("tasks.txt")).thenReturn(mockPath);

            Main.saveTask(tasks);

            mockedFiles.verify(() -> Files.writeString(eq(mockPath), anyString()), times(1));
        }
    }

    @Test
    void test_loadFromTxt() {
        String fileContent = "1. лялафцацф || Выполнена\n2. ацфацфялялля || Не выполнена\n";

        String[] lines = fileContent.split("\n");
        ArrayList<Task> loadedTasks = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] firstSplit = line.split("\\. ", 2);
            if (firstSplit.length < 2) continue;

            int id = Integer.parseInt(firstSplit[0]);
            String rest = firstSplit[1];
            String[] secondSplit = rest.split(" \\|\\| ");
            if (secondSplit.length < 2) continue;

            String description = secondSplit[0];
            boolean isComplete = secondSplit[1].trim().equals("Выполнена");

            loadedTasks.add(new Task(description, isComplete, id));
        }

        assertEquals(2, loadedTasks.size());
        assertEquals("Купить хлеб", loadedTasks.get(0).getDescription());
        assertTrue(loadedTasks.get(0).getIsComplete());
        assertFalse(loadedTasks.get(1).getIsComplete());
    }

    @Test
    void test_saveInJson() {
        tasks.add(new Task("сэйв джсонч", true, 1));

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class);
             MockedStatic<Path> mockedPath = mockStatic(Path.class)) {

            Path mockPath = mock(Path.class);
            when(Path.of("tasks.json")).thenReturn(mockPath);

            Main.saveInJson(tasks);

            mockedFiles.verify(() -> Files.writeString(eq(mockPath), anyString()), times(1));
        }
    }
}
//111