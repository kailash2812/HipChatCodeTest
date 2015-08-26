Hipchat Code Test

Completed the test by creating a small application in Android. User can enter input string and when the user clicks enter the output returns JSON string with special contents (mentions, emoticons, Links)
Assumed any word starts with @  and ends with nonword character as mention and any word in parenthesis and any urls as links as stated in problem statement.
Used Pattern Matchers to identify mentions, emoticons , links and titles.
RegexStringMatcher is the helper class in retrieving special contents from the input string
HipChatCodeTestActivity uses ActivityInstrumentationTestCase2 for functional testing of the Activity
Added documentation in relevant methods and classes
The App uses MainActivity and its corresponding MainFragment for achieving the task with a simple layout of edit text, button and textview.
