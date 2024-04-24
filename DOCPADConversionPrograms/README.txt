These are the programe I used to Fix DOCPAD content to Hugo.

1.) AddHugoEtesterFrontMatterWithSectionMenus: Extends the Frot Matter creater and adds a context-menu variable
2.) AddHugoEtesterFrontMatter: Added front matter to Each Page and Folder
3.) FileRenameToIndexHtml: Create a page bubndle for each HTML page in DOCPAD.  Meaning, it creates teh folder with file "filename" and "copies" the <>file>.html to index.html (not "move")
4.) MathJaxEncodeFixer:  This does a bunch of things:
	a.) Get rid or <center> html tags. and replace with a "<div class="synopsis-image">"  with a class
	b.) Replace "<span lang="latex">"  with  "<span class="latex" lang="latex"> \\("
	c.) Replace followibng close spans "</span>" with " \\) </span>" 
	d.) fix to break up multiline Latex bevcause MathJax 3.0 does not support the default syntax yet.
			// https://stackoverflow.com/questions/4025482/cant-escape-the-backslash-with-regex
			// If you're putting this in a string within a program, you may actually need to use four backslashes (because the string parser will remove two of them when "de-escaping" it for the string, and then the regex needs two for an escaped regex backslash)
	Anytine there are mathjax strings with "\\" to end a string, I enclose the whole string with " \\begin{align} " and " \\end{align} " tags

