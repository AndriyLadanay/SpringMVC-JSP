function previous(page){    //prevent making request to open page with index < 1
    if (page < 1){
        return;
    }
    else {
        location.assign("/employees/" + page);
    }
}

function next(page, lastPage) {    //prevent making request to open page with index > lastPage
    if(page <= lastPage){
        location.assign("/employees/" + page);
    }
    else {
        return;
    }
}

function searchByName(){    //prevent making search request with empty name;
    var name = document.getElementById("name").value;
    if(name.trim() == ''){
        alert("Please, enter name of employee you want to find");
        return;
    }
    location.assign("/employees/search/" + name + "/1");
}

function previousWithName(page, name) {  //prevent making request to open page with index < 1 after user did search by name
    if (page < 1){
        return;
    }
    else {
        location.assign("/employees/search/" + name + "/" + page);
    }
}

function nextWithName(page, lastPage, name) {   //prevent making request to open page with index > lastPage after user did search by name
    if(page <= lastPage){
        location.assign("/employees/search/" + name + "/" + page);
    }
    else {
        return;
    }
}
//checks if first or last page is opened and disable appropriate buttons(previous, first or next, last)
function disableButtons(currentPage, lastPage) {
    if(currentPage == 1){
        document.getElementById("first").disabled = true;
        document.getElementById("previous").disabled = true;
    }
    if(currentPage == lastPage){
        document.getElementById("next").disabled = true;
        document.getElementById("last").disabled = true;
    }
}

function setCurrentValues(dpName, isActive){      //set current values of employee's properties into form's fields
    var x = document.getElementsByTagName("option");
    var y = document.getElementById("isActive");
    for(i = 0; i < x.length; i++){
        if (x[i].text == dpName){
            x[i].selected = true;
        }
    }
    if(isActive == "yes"){
        y.checked = true;
    }
    else {
        y.checked = false;
    }
}

function lastPage() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var lastPage = this.responseText;
            location.assign("/employees/" + lastPage);
        }
    };
    xhttp.open("GET", "employees/lastPage", true);
    xhttp.send();
}