function getQueryVariable(variable)
{
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i=0;i<vars.length;i++) {
		var pair = vars[i].split("=");
		if(pair[0] == variable){
			return pair[1];
		}
	}
	return(false);
}

function showPrintFormatPanelCheck() {
	if (getQueryVariable("formatforprint") != false) {
		document.getElementById("printFormatPanel").classList.remove("default-hidden");
		document.getElementById("container").classList.remove("hide-for-print");
	}
	// add extra div's for formatting - sprinkle enough div's to ensure topic numbers, names, section numbers and names do not run into each other
	addFormattingDivs();
}
function addFormattingDivs() {
	$( "h2.topic-heading, h3.skill-heading, h4.skill-subheading, h5.skill-smallheading, h6.skill-smallsubheading" ).after( "<div />" );
}

function updateChapterHeading() {
	toggleChapterHeading();
	toggleChapterHeading();
}
function toggleChapterHeading() {
//    document.getElementById("chapter-heading").innerHTML = "Chapter Heading";
//    alert("Show Chapter Heading Button Clicked!");

	document.getElementById("chapterHeadingValueDisplay").innerHTML = document.getElementById("chapterHeadingValue").value;
	document.getElementById("chapterHeadingDescriptionDisplay").innerHTML = document.getElementById("chapterHeadingDescription").value;

	// Toggle display on the chapterHeadingDetailsPanel
	document.getElementById("chapterHeadingDetailsPanel").classList.toggle("default-hidden");

    var chapterHeadingDisplayElement = document.getElementById("chapterHeadingDisplay");

//	chapterHeadingElement.classList.toggle("default-hidden");

    if (chapterHeadingDisplayElement.classList.contains("default-hidden")) {
	    chapterHeadingDisplayElement.classList.remove("default-hidden");
	    document.getElementById("chapterHeadingToggleButton").innerHTML="Hide Heading";
	    // add Chapter Number to all (has only 1) topic-heading as a prefix...
		var sesi1 = "<span class='topic-heading-added'>" + document.getElementById("chapterNumberValue").value + ":&nbsp;&nbsp;" + "</span>";
		$( "h2.topic-heading" ).before( sesi1 );
	    // add Chapter Number to all skill-heading's as a prefix...
		var sesi2 = "<span class='skill-heading-added'>" + document.getElementById("chapterNumberValue").value + "." + "</span>";
		$( "h3.skill-heading, h3.solved-examples" ).before( sesi2 );
	    // add Chapter Number to all skill-subheading's as a prefix...
		var sesi3 = "<span class='skill-subheading-added'>" + document.getElementById("chapterNumberValue").value + "." + "</span>";
		$( "h4.skill-subheading" ).before( sesi3 );
	    // add Chapter Number to all skill-smallheading's as a prefix...
		var sesi4 = "<span class='skill-smallheading-added'>" + document.getElementById("chapterNumberValue").value + "." + "</span>";
		$( "h5.skill-smallheading" ).before( sesi4 );
	    // add Chapter Number to all skill-smallsubheading's as a prefix...
		var sesi5 = "<span class='skill-smallsubheading-added'>" + document.getElementById("chapterNumberValue").value + "." + "</span>";
		$( "h6.skill-smallsubheading" ).before( sesi5 );
		// Add any notes pages
		var notesPagesCountValue = document.getElementById("notesPagesCount").value;
		if (notesPagesCountValue < 0 || notesPagesCountValue > 20) {
			alert("Cannot add '" + notesPagesCountValue + "' Pages.  Please add between 0 and 20 Notes pages.");
		} else {
			for(i = 0; i < notesPagesCountValue; i++) {
				$("<div class='notes-pages-added' style='page-break-before:always; font-weight: bold; text-decoration: underline; text-align: center;'>Notes</div>").appendTo(document.body);
			}
		}
		// update TOC each time the TOC is shown
		showTOC(document.getElementById("chapterNumberValue").value);
	} else {
	    chapterHeadingDisplayElement.classList.add("default-hidden");
	    document.getElementById("chapterHeadingToggleButton").innerHTML="Show Heading";
	    // Remove Chapter Number from all (has only one) topic-heading's as a prefix...
		$( ".topic-heading-added" ).remove();
	    // Remove Chapter Number from all skill-heading's as a prefix...
		$( ".skill-heading-added" ).remove();
	    // Remove Chapter Number from all skill-subheading's as a prefix...
		$( ".skill-subheading-added" ).remove();
	    // Remove Chapter Number from all skill-smallheading's as a prefix...
		$( ".skill-smallheading-added" ).remove();
	    // Remove Chapter Number from all skill-smallsubheading's as a prefix...
		$( ".skill-smallsubheading-added" ).remove();
	    // Remove any notes pages created at the end of the Body Section...
		$( ".notes-pages-added" ).remove();
	}
}

function showLogoForPrint() {
    if (document.getElementById("showLogoForPrint").checked) {
	    document.getElementById("logoRow").classList.remove("hide-for-print");
	} else {
	    document.getElementById("logoRow").classList.add("hide-for-print");
	}
}






function showTOC(chapterIndex) {

	// Remove any existing TOC contents
	$("#TOC").empty();

	var ToC = "<h4 class='toc-heading'>Contents:</h4>" +
		    "<ul style='list-style-type:none;'>";

	var el, title, link;
	// chapterIndex = chapterIndex || "0";
	var skillIndex = 0;
	var subskillIndex = 0;

//	$(".topic-heading, .skill-heading, .skill-subheading").each(function() {

	$(".skill-heading, .skill-subheading").each(function() {

		el = $(this);
		title = el.text();
		link = "#";

		if (this.classList.contains("topic-heading")) {
			link = "#" + el.attr("id");
			var tocText = (!chapterIndex || 0 === chapterIndex.length) ? title : chapterIndex + "." + title;
			newLine =
				"<li class='topic-heading-toc-line'>" +
					"<div>" +
						tocText +
					"</div>" +
				"</li>";
		  ToC += newLine;
		  skillIndex = 0;
		  subskillIndex = 0;
		} else if (this.classList.contains("skill-heading")) {
			link = "#" + el.attr("id");
			skillIndex++;
			var tocText = (!chapterIndex || 0 === chapterIndex.length) ? (skillIndex + ".  " + title) : chapterIndex + "." + (skillIndex + ".  " + title);
			newLine =
				"<li class='skill-heading-toc-line'>" +
					"<div><a style='text-decoration: none; color: #126daf;' href='" + link + "'>" +
						tocText +
					"</a></div>" +
				"</li>";
		  ToC += newLine;
		  subskillIndex = 0;
		} else {
			subskillIndex++;
			var tocText = (!chapterIndex || 0 === chapterIndex.length) ? (skillIndex + "." + subskillIndex + ".   " + title) : chapterIndex + "." + (skillIndex + "." + subskillIndex + ".   " + title);
			newLine =
				"<li class='skill-subheading-toc-line'>" +
					"<div>" +
						tocText +
					"</div>" +
				"</li>";
		  ToC += newLine;
		}


	});



	ToC += "</ul>";

	$("#TOC").prepend(ToC);
}
