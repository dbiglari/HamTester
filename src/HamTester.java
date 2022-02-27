import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class HamTester {

	public static String Technician_Class = "assets/tests/2018-2022 Tech Pool.txt";
	public static String General_Class = "assets/tests/2019-2023GeneralClassQuestionPool.txt";
	public static String Extra_Class = "assets/tests/2020ExtraClassPoolJan22.txt";

	public static int NumPracticeTestQuestions = 35;
	public static int PercentRequiredToPass = 75;
	private static boolean DebugTestLoader = false;

	public static void main(String[] args) {
		// display the main menu
		DisplayMenu();
	}

	public static void DisplayMenu() {
		// load the Technician test by default
		Test t = LoadTest(Technician_Class);

		System.out.println(
				"Welcome to HamTester. This tool will present multiple choice questions from the HAM radio exam for the selected test level.");
		System.out.println("Please select from the following options:");
		while (true) {
			System.out.println("C) Change current test (" + t.testfile + ")");
			System.out.println("R) Reset current test");
			System.out.println("A) Ask random questions from the current test");
			System.out.println("S) Start a practice test (" + NumPracticeTestQuestions + " questions, "
					+ PercentRequiredToPass + "% correct to pass)");
			System.out.println("Q) Quit");
			System.out.print("Enter selection>>>");

			String selection = ReadLine();
			if (selection.length() > 0) {
				char c = selection.toUpperCase().charAt(0);
				switch (c) {
				case 'C':
					t = ChangeTest(t);
					break;
				case 'R':
					ResetTest(t);
					break;
				case 'A':
					AskRandomQuestions(t);
					break;
				case 'S':
					GivePracticeTest(NumPracticeTestQuestions, t);
					break;
				case 'Q':
					System.exit(0);
					break;
				}
			}
		}
	}

	private static Test ChangeTest(Test currentTest) {

		while (true) {

			System.out.println("1) Technician class");
			System.out.println("2) General class");
			System.out.println("3) Extra class");
			System.out.println("4) Specify test file to load");
			System.out.println("5) Go back");
			System.out.print("Enter selection>>>");

			String selection = ReadLine();
			if (selection.length() > 0) {
				char c = selection.toUpperCase().charAt(0);
				switch (c) {
				case '1':
					currentTest = LoadTest(Technician_Class);
					return currentTest;
				case '2':
					currentTest = LoadTest(General_Class);
					return currentTest;
				case '3':
					currentTest = LoadTest(Extra_Class);
					return currentTest;
				case '4':
					System.out.print("Enter file name to load (absolute path or relative to current directory: "
							+ System.getProperty("user.dir") + " ) >>>");
					String filename = "";
					currentTest = LoadTest(filename);
					return currentTest;
				case '5':
					return currentTest;
				}
			}
		}

	}

	public static void GivePracticeTest(int numquestions, Test t) {
		// clear all the test questions
		ResetTest(t);
		t.practicetestactive = true;
		Random r = new Random();
		// loop through the test, asking random questions
		for (int i = 0; i < numquestions; i++) {

			// find a question that hasn't been asked yet
			int k = r.nextInt(t.subelements.size());
			int m = r.nextInt(((SubElement) t.subelements.get(k)).testareas.size());
			int j = r.nextInt(((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions.size());
			while (((TestQuestion) ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions
					.get(j)).hasquestionbeenasked == true) {
				j = r.nextInt(((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions.size());
			}

			if (AskQuestion(
					((TestQuestion) ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions.get(j)),
					t) == true) {
				t.correctquestions++;
			}
			t.questionsasked++;
			DisplayTestState(t);
			if (t.practicetestactive == false) {
				System.out.println("Practice Test aborted.");
				return;
			}
		}
	}

	private static void DisplayTestState(Test t) {

		float percent = ((float) 100.0) * ((float) t.correctquestions) / ((float) t.questionsasked);
		int remainingquestions = HamTester.NumPracticeTestQuestions - t.questionsasked;
		System.out.print(t.correctquestions + " questions answered correctly, ( " + percent + "% ");
		if (percent >= HamTester.PercentRequiredToPass) {
			System.out.print("passing )");
		} else {
			System.out.print("not passing )");
		}
		System.out.println(" " + remainingquestions + " questions remaining.");
	}

	private static void ResetTest(Test t) {
		for (int k = 0; k < t.subelements.size(); k++) {
			for (int m = 0; m < ((SubElement) t.subelements.get(k)).testareas.size(); m++) {
				for (int i = 0; i < ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions
						.size(); i++) {
					((TestQuestion) ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions
							.get(i)).answergiven = null;
					((TestQuestion) ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions
							.get(i)).hasquestionbeenasked = false;
				}
			}
		}
		t.correctquestions = 0;
		t.questionsasked = 0;
	}

	public static void AskRandomQuestions(Test t) {
		t.practicetestactive = true;
		while (t.practicetestactive == true) {
			Random r = new Random();
			// find a question that hasn't been asked yet
			int k = r.nextInt(t.subelements.size());
			int m = r.nextInt(((SubElement) t.subelements.get(k)).testareas.size());
			int j = r.nextInt(((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions.size());
			while (((TestQuestion) ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions
					.get(j)).hasquestionbeenasked == true) {
				k = r.nextInt(t.subelements.size());
				m = r.nextInt(((SubElement) t.subelements.get(k)).testareas.size());
				j = r.nextInt(((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions.size());
			}

			AskQuestion(
					((TestQuestion) ((TestArea) ((SubElement) t.subelements.get(k)).testareas.get(m)).questions.get(j)),
					t);
		}
	}

	public static String ParseAnswer(String inputtext) {
		String answer = "";

		for (int i = 0; i < inputtext.length(); i++) {
			char c = inputtext.charAt(i);

			if (c == '(') {
				answer = "" + inputtext.charAt(i + 1);
				return answer;
			}

		}

		// couldn't find an answer!
		System.out.println("Error parsing test!");
		System.exit(0);
		return null;
	}

	public static Test LoadTest(String filename) {
		int totalquestionsloaded = 0;
		// load the test file into array of TestQuestions
		Test t = new Test();
		t.testfile = filename;

		SubElement currentSubElement = null;
		TestArea currentTestArea = null;
		TestQuestion currentTestQuestion = null;

		int state = TestFileState.Header;
		// open the file
		try {
			FileInputStream fis = new FileInputStream(t.testfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;

			// read test line by line
			while ((line = br.readLine()) != null) {
				// process the line.
				boolean isSubElement = IsLineSubelement(line);
				if (state == TestFileState.Header) {

					if (isSubElement == true) {
						state = TestFileState.Subelement;
						currentSubElement = new SubElement();
						t.subelements.add(currentSubElement);

						// read id and description
						String id = GetSecondWord(line).substring(0, 2);
						int index;

						index = ("SUBELEMENT " + id + " - ").length();
						String description = line.substring(index);

						currentSubElement.ID = id;
						currentSubElement.Description = description;
						currentSubElement.num = 1;
					} else {
						// add this line to testinfo
						t.testinfo += line + "\n";
					}
				} else if (state == TestFileState.Subelement) {
					// first testarea after subelement start
					if (line.startsWith(currentSubElement.ID + "A ")) {
						state = TestFileState.TestArea;

						String id = currentSubElement.ID + "A";

						int beginindex = (id + " - ").length();

						String description = line.substring(beginindex);

						currentTestArea = new TestArea();
						currentTestArea.TestAreaLetter = 'A';
						currentTestArea.ID = id;
						currentTestArea.Description = description;

						if (DebugTestLoader) {
							System.out.println(currentTestArea.ID + " : " + totalquestionsloaded);
							System.out.flush();
						}

						currentSubElement.testareas.add(currentTestArea);
					}

				} else if (state == TestFileState.TestArea) {
					if (line.startsWith(currentTestArea.ID)) {
						state = TestFileState.Question;

						String id = currentTestArea.ID + "01 ";
						int beginindex = 0;

						currentTestQuestion = new TestQuestion();
						currentTestArea.questions.add(currentTestQuestion);
						totalquestionsloaded++;

						// parse the answer
						String answer = ParseAnswer(line);
						currentTestQuestion.ID = id.trim();
						currentTestQuestion.reference = line;

						currentTestQuestion.correctanswer = answer;
						// the next line is the question text
						currentTestQuestion.questiontext = br.readLine() + "\n";
						currentTestQuestion.num = 1;

						line = br.readLine();
						// read until ~~
						while (!line.startsWith("~~")) {
							currentTestQuestion.questiontext += line.substring(beginindex) + "\n";
							line = br.readLine();
						}
					}
				} else if (state == TestFileState.Question) {
					char nextTestAreaLetter = currentTestArea.TestAreaLetter;
					nextTestAreaLetter++;
					String nextTestArea = currentSubElement.ID + Character.toString(nextTestAreaLetter);
					// read until ~~
					if (line.startsWith(currentTestArea.ID)) {
						if (DebugTestLoader) {
							System.out.println("Parsing question:" + totalquestionsloaded);
							System.out.flush();
						}

						int currentQuestionNum = currentTestQuestion.num;
						state = TestFileState.Question;

						String formattednum = FormatString(currentQuestionNum + 1);
						String id = currentTestArea.ID + formattednum;
						int beginindex = 0;

						currentTestQuestion = new TestQuestion();
						currentTestArea.questions.add(currentTestQuestion);
						totalquestionsloaded++;

						// parse the answer
						String answer = ParseAnswer(line);
						currentTestQuestion.ID = id.trim();
						currentTestQuestion.reference = line;
						currentTestQuestion.correctanswer = answer;
						// the next line is the question text
						currentTestQuestion.questiontext = br.readLine() + "\n";

						currentTestQuestion.num = currentQuestionNum + 1;

						line = br.readLine();
						// read until ~~
						while (!line.startsWith("~~")) {
							currentTestQuestion.questiontext += line.substring(beginindex) + "\n";
							line = br.readLine();
						}

						if (DebugTestLoader) {
							System.out.println("Finished parsing question:" + totalquestionsloaded);
							System.out.flush();
						}
					} else if (isSubElement) {
						int currentSubElementNum = currentSubElement.num;
						// we found the next subelement
						state = TestFileState.Subelement;
						currentSubElement = new SubElement();
						t.subelements.add(currentSubElement);

						// read id and description
						String id = GetSecondWord(line).substring(0, 2);
						int index;

						index = ("SUBELEMENT " + id + " - ").length();
						String description = line.substring(index);

						currentSubElement.ID = id;
						if (DebugTestLoader) {
							System.out.println(currentSubElement.ID + " : " + totalquestionsloaded);
							System.out.flush();
						}

						currentSubElement.Description = description;
						currentSubElement.num = currentSubElementNum + 1;
						if (currentSubElement.num > 9) {
							currentSubElement.num = 0;
						}
					} else if (line.startsWith(nextTestArea)) {
						state = TestFileState.TestArea;

						String id = nextTestArea;

						int beginindex = (id + " - ").length();

						String description = line.substring(beginindex);

						currentTestArea = new TestArea();
						currentTestArea.TestAreaLetter = nextTestAreaLetter;
						currentTestArea.ID = id;
						currentTestArea.Description = description;

						if (DebugTestLoader) {
							System.out.println(currentTestArea.ID + " : " + totalquestionsloaded);
							System.out.flush();
						}

						currentSubElement.testareas.add(currentTestArea);

					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Total Questions Loaded: " + totalquestionsloaded);
		return t;
	}

	private static String GetSecondWord(String line) {
		String outputtext = "";
		int wordnum = 1;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == ' ') {
				wordnum++;
				if (wordnum > 2)
					return outputtext;
			} else if (wordnum == 2) {
				outputtext += c;
			}
		}

		return outputtext;
	}

	private static String FormatString(int i) {

		String s = Integer.toString(i);
		return s;
	}

	public static boolean IsLineSubelement(String line) {

		if (line.startsWith("SUBELEMENT") && !line.endsWith("Questions")) {
			return true;
		}
		return false;
	}

	public static String ReadLine() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name = "";
		try {
			name = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;

	}

	// Some of the strings in the test file have unprintable characters.
	// Remove these characters
	public static String CleanText(String inputtext) {
		String outputtext = "";
		for (int i = 0; i < inputtext.length(); i++) {
			char c = inputtext.charAt(i);
			int j = (int) c;
			if (j == 160) {
				outputtext += ' ';
			} else if (j >= 256) {

				if (j == 65533) {
					outputtext += '\'';
				}

			} else {
				outputtext += inputtext.charAt(i);
			}
		}

		return outputtext;
	}

	public static boolean AskQuestion(TestQuestion q, Test t) {
		q.correctlyanswered = false;

		// display text of question
		if (q.questiontext == null || q.correctanswer == null) {
			System.out.println("A test question was incorrectly loaded.  Please check input files.");
			System.exit(0);
		}
		System.out.println("[" + q.ID + "]");
		System.out.println(CleanText(q.questiontext));
		System.out.println("E. Return to main menu");

		q.hasquestionbeenasked = true;

		// wait for keyboard input
		System.out.print(">>>");

		String selection = ReadLine();

		if (selection.toUpperCase().trim().equals("E")) {
			t.practicetestactive = false;
			return false;
		} else if (selection.toUpperCase().trim().equals("")) {
			System.out.println("The correct answer is " + q.correctanswer.toUpperCase());
		} else if (selection.toUpperCase().trim().equals(q.correctanswer.toUpperCase().trim())) {
			// if correct answer is given, return true
			q.correctlyanswered = true;
			System.out.println(q.correctanswer.toUpperCase() + " is the correct answer!");
		} else {
			System.out.println("Sorry, " + selection.toUpperCase().trim() + " is not the correct answer. "
					+ q.correctanswer.toUpperCase() + " is the correct answer!");
		}

		System.out.println("Press enter to continue");

		// otherwise return false
		return q.correctlyanswered;

	}

}
