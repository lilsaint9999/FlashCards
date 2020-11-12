import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FlashCards
{
    public static void main(String[] args)
    {
        System.out.println("yes we`ve made some changes here");
        Scanner scanner = new Scanner(System.in);
        // this is the small change , so I can try push the code back to repos in github
        // trying to commit a change and then push it to original repository
        boolean exit = false;
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> errorMap = new HashMap<>();
        int count = 0;
        Integer harderst = 0;
        List<String> log= new ArrayList<>();
        boolean exports = false;
        int impElement = 0;
        int expElement = 0;
        boolean imports = false;
        for (int i =0; i<args.length;i++){
            if ("-import".equals(args[i])){
                imports=true;
                impElement = i+1;
            }else if("-export".equals(args[i])){
                exports=true;
                expElement=i+1;
            }
        }
        while (!exit)
        {   if(imports){

            try
            {
                String path = args[impElement];
                scanner = new Scanner(new File(path));
                count = 0;
                String key = null;
                while (scanner.hasNext())
                {
                    if (count % 2 == 0)
                    {
                        key = scanner.nextLine();
                        if (!map.containsKey(key))
                        {
                            map.put(key, null);
                            errorMap.put(key, new Integer(0));
                        }
                    }
                    else
                    {
                        map.replace(key, scanner.nextLine());
                        int temp = Integer.parseInt(scanner.nextLine());
                        errorMap.replace(key,temp);
                    }
                    count++;
                }
                scanner = new Scanner(System.in);
                System.out.println(count / 2 + " cards have been loaded. Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
                log.add(count / 2 + " cards have been loaded. Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
                imports=false;
            }
            catch (FileNotFoundException e)
            {
                System.out.print("File not found.");
                log.add("File not found.");
            }

        }
            System.out.println(
                    "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            log.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String action = scanner.nextLine();
            log.add(action);
            action:
            if (action.equals("add"))
            {
                System.out.print("The card:\n");
                log.add("The card:\n");
                String term = scanner.nextLine();
                log.add(term);
                if (map.containsKey(term))
                {
                    System.out.println("The card \"" + term + "\" already exists.");
                    log.add("The card \"" + term + "\" already exists.");
                    break action;
                }
                else
                {
                    map.put(term, null);
                    errorMap.put(term, new Integer(0));
                }
                System.out.print("The definition of the card:\n");
                log.add("The definition of the card:\n");
                String definition = scanner.nextLine();
                log.add(definition);
                if (map.containsValue(definition))
                {
                    System.out.println("The definition \"" + definition + "\" already exists.");
                    log.add("The definition \"" + definition + "\" already exists.");
                    map.remove(term);
                    break action;
                }
                else
                {
                    System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
                    log.add("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
                    map.putIfAbsent(term, definition);
                    break action;
                }

            }
            else if (action.equals("remove"))
            {
                System.out.print("The card:\n");
                log.add("The card:\n");
                String cardRemove = scanner.nextLine();
                log.add(cardRemove);
                if (map.containsKey(cardRemove))
                {
                    map.remove(cardRemove);
                    errorMap.remove(cardRemove);
                    System.out.println("The card has been removed.");
                    log.add("The card has been removed.");
                    break action;
                }
                else
                {
                    System.out.println("Can't remove \"" + cardRemove + "\": there is no such card.");
                    log.add("Can't remove \"" + cardRemove + "\": there is no such card.");
                    break action;
                }
            }
            else if (action.equals("import"))
            {
                try
                {
                    System.out.println("File name:");
                    log.add("File name:");
                    String path = scanner.nextLine();
                    log.add(path);
                    scanner = new Scanner(new File(path));
                    count = 0;
                    String key = null;
                    while (scanner.hasNext())
                    {
                        if (count % 2 == 0)
                        {
                            key = scanner.nextLine();
                            if (!map.containsKey(key))
                            {
                                map.put(key, null);
                                errorMap.put(key, new Integer(0));
                            }
                        }
                        else
                        {
                            map.replace(key, scanner.nextLine());
                            int temp = Integer.parseInt(scanner.nextLine());
                            errorMap.replace(key,temp);
                        }
                        count++;
                    }
                    scanner = new Scanner(System.in);
                    System.out.println(count / 2 + " cards have been loaded.");
                    log.add(count / 2 + " cards have been loaded.");
                    break action;
                }
                catch (FileNotFoundException e)
                {
                    System.out.println("File not found.");
                    log.add("File not found.");
                    break action;
                }
            }
            else if (action.equals("export"))
            {
                System.out.println("File name:");
                log.add("File name:");
                String path = scanner.nextLine();
                log.add(path);
                File file = new File(path);
                count = 0;
                try (PrintWriter printWriter = new PrintWriter(file))
                {
                    for (Entry<String, String> entry : map.entrySet())
                    {
                        printWriter.println(entry.getKey());
                        printWriter.println(entry.getValue());
                        printWriter.println(errorMap.get(entry.getKey()));
                        count++;
                    }
                    System.out.println(count + " cards have been saved.");
                    log.add(count + " cards have been saved.");
                    break action;
                }
                catch (IOException e)
                {
                    System.out.printf("An exception occurs %s", e.getMessage());
                    break action;
                }
            }
            else if (action.equals("ask"))
            {
                System.out.println("How many times to ask?");
                log.add("How many times to ask?");
                count = scanner.nextInt();
                log.add(String.valueOf(count));
                scanner.nextLine();
                for (int i = 1; i <= count; i++)
                {
                    List<String> keysList = new ArrayList<>(map.keySet());
                    String randomKey = keysList.get(new Random().nextInt(keysList.size()));
                    System.out.println("Print the definition of \"" + randomKey + "\":");
                    log.add("Print the definition of \"" + randomKey + "\":");
                    String answer = scanner.nextLine();
                    if (answer.equals(map.get(randomKey)))
                    {
                        System.out.println("Correct answer.");
                        log.add("Correct answer.");
                    }
                    else if (map.containsValue(answer))
                    {
                        System.out.println("Wrong answer. The correct one is \"" + map.get(randomKey) + "\", you've just written the " +
                                "definition of \"" + getKey(map, answer) + "\".");
                        log.add("Wrong answer. The correct one is \"" + map.get(randomKey) + "\", you've just written " +
                                "the " +
                                "definition of \"" + getKey(map, answer) + "\".");
                        Integer errors = new Integer(errorMap.get(randomKey).intValue() + 1);
                        errorMap.replace(randomKey, errors);
                    }
                    else
                    {
                        System.out.println("Wrong answer. The correct one is \"" + map.get(randomKey) + "\".");
                        log.add("Wrong answer. The correct one is \"" + map.get(randomKey) + "\".");
                        Integer errors = new Integer(errorMap.get(randomKey).intValue() + 1);
                        errorMap.replace(randomKey, errors);
                    }
                }
                count = 0;
                break action;
            }
            else if (action.equals("exit"))
            {
                System.out.println("Bye bye!");
                log.add("Bye bye!");
                if(exports){
                    File file = new File(args[expElement]);
                    count = 0;
                    try (PrintWriter printWriter = new PrintWriter(file))
                    {
                        for (Entry<String, String> entry : map.entrySet())
                        {
                            printWriter.println(entry.getKey());
                            printWriter.println(entry.getValue());
                            printWriter.println(errorMap.get(entry.getKey()));
                            count++;
                        }
                        System.out.println(count + " cards have been saved.");
                        log.add(count + " cards have been saved.");

                    }
                    catch (IOException e)
                    {
                        System.out.printf("An exception occurs %s", e.getMessage());

                    }
                }
                exit = true;
                break action;
            }
            else if (action.equals("log"))
            {
                System.out.println("File name:");
                File file = new File(scanner.nextLine());

                try (PrintWriter printWriter = new PrintWriter(file))
                {
                    for(String line:log){
                        printWriter.println(line);
                    }
                    System.out.println("The log has been saved.");
                    break action;
                }
                catch (IOException e)
                {
                    System.out.printf("An exception occurs %s", e.getMessage());
                    break action;
                }
            }
            else if (action.equals("hardest card"))
            {
                for (Entry<String, Integer> entry : errorMap.entrySet())
                {
                    if (entry.getValue() > harderst)
                    {
                        harderst = entry.getValue();
                    }
                }
                if (checkNoErrors(errorMap))
                {
                    System.out.println("There are no cards with errors.");
                    log.add("There are no cards with errors.");
                    break action;
                }
                else if (checkDuplicates(errorMap, harderst))
                {
                    Map<Integer, ArrayList<String>> reverseMap = new HashMap<>(
                            errorMap.entrySet().stream()
                                    .collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream()
                                    .collect(Collectors.toMap(
                                            item -> item.get(0).getValue(),
                                            item -> new ArrayList<>(
                                                    item.stream()
                                                            .map(Map.Entry::getKey)
                                                            .collect(Collectors.toList())
                                            ))
                                    ));
                    ArrayList<String> list = reverseMap.get(harderst);
                    StringBuilder str = new StringBuilder();
                    for (int i = 1; i <= list.size(); i++)
                    {
                        if (i != list.size())
                        {
                            str.append("\"");
                            str.append(list.get(i - 1));
                            str.append("\", ");
                        }
                        else
                        {
                            str.append("\"");
                            str.append(list.get(i - 1));
                            str.append("\".");
                        }
                    }
                    System.out.println("The hardest cards are " + str.toString() + " You have " + harderst + " errors answering them.");
                    log.add("The hardest cards are " + str.toString() + " You have " + harderst + " errors answering " +
                            "them.");
                    break action;
                }
                else
                {
                    System.out.println("The hardest card is \"" + getKey(
                            errorMap,
                            harderst) + "\". You have " + harderst + " errors answering it.");
                    log.add("The hardest card is \"" + getKey(
                            errorMap,
                            harderst) + "\". You have " + harderst + " errors answering it.");
                    break action;
                }
            }
            else if (action.equals("reset stats"))
            {
                for (Entry<String, Integer> entry : errorMap.entrySet())
                {
                    errorMap.replace(entry.getKey(), new Integer(0));
                }
                harderst = 0;
                System.out.println("Card statistics has been reset.");
                log.add("Card statistics has been reset.");
            }
            else if (!action.equals("exit") || !action.equals("remove") || !action.equals("add") || !action.equals(
                    "import") || !action.equals(
                    "export") || !action.equals("ask") || !action.equals("log") || !action.equals("hardest card") || !action.equals(
                    "reset stats"))
            {
                break action;
            }
        }
    }


    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Entry<K, V> entry : map.entrySet())
        {
            if (entry.getValue().equals(value))
            {
                return entry.getKey();
            }
        }
        return null;
    }


    public static boolean checkNoErrors(Map<String, Integer> map)
    {
        int internalCount = 0;
        for (Entry<String, Integer> entry : map.entrySet())
        {
            if (!entry.getValue().equals(new Integer(0)))
            {
                return false;
            }
        }
        return true;
    }


    public static boolean checkDuplicates(Map<String, Integer> map, int hardest)
    {
        int internalCount = 0;
        for (Entry<String, Integer> entry : map.entrySet())
        {
            if (entry.getValue().equals(hardest))
            {
                internalCount++;
            }
        }
        if (internalCount > 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
