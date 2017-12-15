function previous(page){
    if (page < 1){
        return;
    }
    else {
        location.assign("/employees/" + page);
    }
}
function next(page, lastPage) {
    if(page <= lastPage){
        location.assign("/employees/" + page);
    }
    else {
        return;
    }
}
function searchByName(){
    var name = document.getElementById("name").value;
    if(name.trim() == ''){
        alert("Please, enter name of employee you want to find");
        return;
    }
    location.assign("/employees/search/" + name + "/1");
}
function previousWithName(page, name) {
    if (page < 1){
        return;
    }
    else {
        location.assign("/employees/search/" + name + "/" + page);
    }
}
function nextWithName(page, lastPage, name) {
    if(page <= lastPage){
        location.assign("/employees/search/" + name + "/" + page);
    }
    else {
        return;
    }
}