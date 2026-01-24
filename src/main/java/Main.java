import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


class Main {

    public static void clear() {
        try {
            if (System.console() != null) {
                System.out.print("\033[H\033[2J\033[3J");
            } else {
                for (int i = 0; i < 80; i++) System.out.println();
            }
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 80; i++) System.out.println();
        }
    }

    public static void printTasks(ArrayList<Task> tasks) {
        clear();
        if (tasks.isEmpty()) {
            System.out.println("бездельник нет заданий у тя\n");
            return;
        }

        for (Task task : tasks) {
            if (task.getIsComplete()) {
                System.out.println(task.getId() + ". " + task.getDescription() + " || Выполнена");
            } else {
                System.out.println(task.getId() + ". " + task.getDescription() + " || Не выполнена");
            }
        }
    }

    public static void addTask(ArrayList<Task> tasks, Scanner sc) {
        clear();
        while (true) {
            System.out.println("че делать собрался?");
            String description = sc.nextLine();

            if (description.isEmpty()) {
                clear();
                System.out.println("я тож люблю ниче не делать но добавь описание задачи пж");
                continue;
            }

            for (int i = 0; i < tasks.size(); i++) {
                tasks.get(i).setId(i + 1);
            }
            int id = tasks.size() + 1;

            while (true) {
                System.out.println("\nзадача комплит? ");
                System.out.println("1. Да");
                System.out.println("2. Нет");

                try {
                    int yes_or_no = sc.nextInt();
                    sc.nextLine();
                    if (yes_or_no == 1) {
                        Task newTask = new Task(description, true, id);
                        tasks.add(newTask);
                        clear();
                        System.out.println("Задача добавлена\n");
                        return;
                    } else if (yes_or_no == 2) {
                        Task newTask = new Task(description, false, id);
                        tasks.add(newTask);
                        clear();
                        System.out.println("Задача добавлена\n");
                        return;
                    } else {
                        clear();
                        System.out.println("1 или 2 идиотина");
                    }
                } catch (InputMismatchException e) {
                    clear();
                    System.out.print("\nчисло введи дурное");
                    sc.nextLine();
                }
            }
        }
    }

    public static void deleteTask(ArrayList<Task> tasks, Scanner sc) {
        if (tasks.isEmpty()) {
            clear();
            System.out.println("бездельник нет заданий у тя\n");
            return;
        }

        printTasks(tasks);

        while (true) {
            try {
                System.out.println("\nномер задачи для удаления:");
                int id = sc.nextInt();
                sc.nextLine();

                boolean flag = false;
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId() == id) {
                        tasks.remove(i);
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    System.out.print("\nнет такого задания");
                    continue;
                }


                for (int i = 0; i < tasks.size(); i++) {
                    tasks.get(i).setId(i + 1);
                }

                clear();
                System.out.println("удалено\n");
                return;

            } catch (InputMismatchException e) {
                clear();
                System.out.print("\nчисло введи дурное");
                sc.nextLine();
            }
        }
    }

    public static void editTask(ArrayList<Task> tasks, Scanner sc) {
        if (tasks.isEmpty()) {
            clear();
            System.out.println("бездельник нет заданий у тя\n");
            return;
        }

        printTasks(tasks);

        while (true) {
            try {
                System.out.println("\nномер задачи для поменяния:");
                int id = sc.nextInt();
                sc.nextLine();

                boolean found = false;
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getId() == id) {
                        found = true;
                        while (true) {
                            clear();
                            System.out.println("\nЧто вы хотите поменять?");
                            System.out.println("1. дескрипшон");
                            System.out.println("2. комплит");

                            int choice = sc.nextInt();
                            sc.nextLine();

                            if (choice == 1) {
                                clear();
                                System.out.println("пиши");
                                String newDescription = sc.nextLine();
                                tasks.get(i).setDescription(newDescription);
                                clear();
                                System.out.println("поменяли задачку\n");
                                return;
                            } else if (choice == 2) {
                                boolean current = tasks.get(i).getIsComplete();
                                tasks.get(i).setIsComplete(!current);
                                clear();
                                System.out.println("поменяли задачку\n");
                                return;
                            } else {
                                clear();
                                System.out.println("только 1 или 2 идиотина");
                            }
                        }
                    }
                }

                System.out.println("нет такого емае");

            } catch (InputMismatchException e) {
                clear();
                System.out.print("\nчисло введи дурное");
                sc.nextLine();
            }
        }
    }

    public static void saveTask(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            clear();
            System.out.println("бездельник нет заданий у тя\n");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (Task task : tasks) {
            sb.append(task.getId())
                    .append(". ")
                    .append(task.getDescription())
                    .append(" || ");

            if (task.getIsComplete()) {
                sb.append("Выполнена");
            } else {
                sb.append("Не выполнена");
            }

            sb.append("\n");
        }

        try {
            Files.writeString(Path.of("tasks.txt"), sb.toString());
            System.out.println("ура успешно сохранил");
        } catch (IOException e) {
            System.out.println("произошла ашибочка");
        }
    }

    public static void loadFromTxt(ArrayList<Task> tasks) {
        try {
            Path filePath = Path.of("tasks.txt");
            if (!Files.exists(filePath)) {
                clear();
                System.out.println("файла tasks.txt нету\n");
                return;
            }

            String content = Files.readString(filePath);
            String[] lines = content.split("\n");

            ArrayList<Task> loadedTasks = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] firstSplit = line.split("\\. ", 2);
                    if (firstSplit.length < 2) continue;

                    int id = Integer.parseInt(firstSplit[0]);
                    String rest = firstSplit[1];

                    String[] secondSplit = rest.split(" \\|\\| ");
                    if (secondSplit.length < 2) continue;

                    String description = secondSplit[0];
                    String status = secondSplit[1].trim();
                    boolean isComplete = status.equals("Выполнена");

                    Task task = new Task(description, isComplete, id);
                    loadedTasks.add(task);

                } catch (Exception e) {
                    System.out.println("Ошибка парсинга строки: " + line);
                }
            }

            tasks.clear();
            tasks.addAll(loadedTasks);

            for (int i = 0; i < tasks.size(); i++) {
                tasks.get(i).setId(i + 1);
            }

            clear();
            System.out.println("Задачи загружены из tasks.txt (" + tasks.size() + " задач)");

        } catch (IOException e) {
            clear();
            System.out.println("произошла ашибочка при чтении файла");
        }
    }

    public static void saveInJson (ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            clear();
            System.out.println("бездельник нет заданий у тя\n");
            return;
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(tasks);

        try {
            Files.writeString(Path.of("tasks.json"), json);
            clear();
            System.out.println("Задачи сохранены в tasks.json");
        } catch (IOException e) {
            System.out.println("произошла ашибочка");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> task_list = new ArrayList<>();

        while (true) {
            System.out.println();
            System.out.println("1. Посмотреть задачи ");
            System.out.println("2. Добавить задачу");
            System.out.println("3. Удалить задачу");
            System.out.println("4. Редактировать задачу");
            System.out.println("5. Сохранить в txt");
            System.out.println("6. Загрузить из txt");
            System.out.println("6. Сохранить в json");
            System.out.println("0. Выход\n");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        printTasks(task_list);
                        continue;
                    case 2:
                        addTask(task_list, sc);
                        continue;
                    case 3:
                        deleteTask(task_list, sc);
                        continue;
                    case 4:
                        editTask(task_list, sc);
                        continue;
                    case 5:
                        saveTask(task_list);
                        continue;
                    case 6:
                        loadFromTxt(task_list);
                        continue;
                    case 7:
                        saveInJson(task_list);
                        continue;
                    case 0:
                        clear();
                        System.out.println("пока лох");
                        return;
                    default:
                        System.out.println("воооо дурак\nпредложенное число введи ");
                }
            } catch (InputMismatchException e) {
                clear();
                System.out.println("ЧИСЛО!!");
                sc.nextLine();
            }
        }
    }
}


