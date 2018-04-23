<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Show All Users</title>
</head>

<body>
  <script src="<c:url value="/resources/pager.js"/>"></script>
  <button onclick="location.href='/employees/search/${nameToSearch}/1'"> << </button>
  <button onclick="previousWithName(${currentPage} - 1, '${nameToSearch}')"> < </button>
  <c:forEach items="${numbers}" var="number">
    <a href="/employees/search/${nameToSearch}/${number}"> ${number} </a>
  </c:forEach>
  <button onclick="nextWithName(${currentPage} + 1, ${lastPage}, '${nameToSearch}')"> > </button>
  <button onclick="location.href='/employees/search/${nameToSearch}/${lastPage}'"> >> </button>
  <table border=1>
    <thead>
      <tr>
        <th></th>
        <th></th>
        <th>empID</th>
        <th>empName</th>
        <th>empActive</th>
        <th>empDepartment</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${employees}" var="employee">
        <tr>
          <td><a href="/employees/view/<c:out value="${employee.id}"/>">View</a></td>
          <td><a href="/employees/edit/<c:out value="${employee.id}"/>">Edit</a></td>
          <td><c:out value="${employee.id}" /></td>
          <td><c:out value="${employee.name}" /></td>
          <td><c:out value="${employee.active}" /></td>
          <td><c:out value="${employee.department.name}" /></td>
          <td><a href="/employees/delete/<c:out value="${employee.id}"/>">Delete</a></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <p> <a href="/employees/insert"> Add employee </a> </p>
  <form name="searchForm">
    <p> EmployeeName</p>
    <p> <input type="text" id="name" name="name"></p>
    <p> <input type="button" value="Search" onclick="searchByName()"> </p>
  </form>
</body>
</html>
