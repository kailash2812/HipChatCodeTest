Hipchat Code Test

1) Completed the test by creating a small application in Android.
2) User can enter input string and when the user clicks enter the output returns JSON string with
special contents (mentions, emoticons, Links)
3) Assumed any word starts with @  and ends with nonword character as mention and any word in parenthesis
upto length 15 and any urls which follow this format (http://www.XXXX.com) as links
as stated in problem statement.
4) Used Pattern Matchers to identify mentions, emoticons , links and titles.
5) RegexStringMatcher is the helper class in retrieving special contents from the input string
6) HipChatCodeTestActivity uses ActivityInstrumentationTestCase2 for functional testing of the Activity
7) Added documentation in relevant methods and classes
8) The App uses MainActivity and its corresponding MainFragment for achieving the task with a simple
layout of edit text, button and textview.
