/** checks value of date string in format mm/dd/yyyy */
function isDateFormatOk(dateStr){
	if (dateStr == "") return true;
	
	var dateArray = explodeArray(dateStr, "/");
	
	var date_month = dateArray[0];
	var date_day = dateArray[1];
	var date_year = dateArray[2];
	
	if(!(isNaN(date_month) || isNaN(date_day) || isNaN(date_year))) {
		return isdate(date_month, date_day, date_year);
	}

	return false;		
}

/**
 * This function is used to split date in format mm/dd/yyyy to array with 3
 * elements: day, month, year.
 */
function explodeArray(item, delimiter) {
	var tempArray = new Array(1);
	var count = 0;
	var tempString = new String(item);

	while (tempString.indexOf(delimiter) > 0) {
		tempArray[count] = tempString.substr(0, tempString.indexOf(delimiter));
		tempString = tempString.substr(tempString.indexOf(delimiter) + 1, tempString.length-tempString.indexOf(delimiter) + 1); 
		count=count+1
	}

	tempArray[count] = tempString;
	return tempArray;
}

/** checks date combination of month day and year. */
function isdate(pMonth, pDay, pYear) {

	int_month=parseInt(pMonth,10);
	int_day=parseInt(pDay,10);
	int_year=parseInt(pYear);
	int_year_size=pYear.length;

	// checking whole date format
	if(!isNaN(int_month) && !isNaN(int_day) && !isNaN(int_year) && int_month > 0 && int_month < 13 && int_day > 0 && int_day < 32 && int_year_size > 3) {
		if(int_month == 2) {
			if(int_year % 4 == 0 && (int_year % 100 != 0 || int_year % 400 == 0)) {
				return (int_day <= 29) ? true : false;
			} else {
				return (int_day <= 28) ? true : false;
			}
		} else {
			if((int_month < 8 && int_month % 2 != 0) || (int_month > 7 && int_month % 2 == 0)) {
				return (int_day <= 31) ? true : false;
			} else {
				return (int_day <= 30) ? true : false;
			}
		}
	} else {
		return false;
	}
}

function isValidZip(elementValue) {
	if (elementValue == "") return true;

	var zipCodePattern = /^\d{5}$|^\d{5}-\d{4}$/;
    return zipCodePattern.test(elementValue);
}

function isValidEmail(elementValue) {
	if (elementValue == "") return true;
	
	var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;   
	return emailPattern.test(elementValue);  
} 

function isValidPhone(elementValue) {
	if (elementValue == "") return true;

	var phoneNumberPattern = /^\(?(\d{3})\)?[- ]?(\d{3})[- ]?(\d{4})$/;   
	return phoneNumberPattern.test(elementValue);   		
}  

function isValidSsn(elementValue){   
	if (elementValue == "") return true;

	var  ssnPattern = /^[0-9]{3}\-?[0-9]{2}\-?[0-9]{4}$/;   
	return ssnPattern.test(elementValue);   
}

function isFromBeforeTo(yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo) {
	var dateFrom = new Date(yearFrom, monthFrom-1, dayFrom);
	var dateTo = new Date(yearTo, monthTo-1, dayTo);
	return dateFrom <= dateTo;
}

