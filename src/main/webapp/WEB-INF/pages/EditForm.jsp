<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
  <title>Edit Employee</title>
</head>
<body>
  <script src="/resources/pager.js"></script>
  <form method="post" action="/employees/edit">
    <p> <input type="hidden" name="id" value="<c:out value="${employee.id}"/>"/> </p>
    <p> empName <input type="text" name="name" value="${employee.name}"> </p>
    <p> empActive <input type="checkbox" id="isActive" name="active"> </p>
    <p> empDepartment
      <select name="dpID">
        <c:forEach items="${departments}" var="department">
          <option value="<c:out value="${department.ID}"/>">
            <c:out value="${department.name}" />
          </option>
        </c:forEach>
      </select>
    </p>
    <p><input type="submit"  value="Save">
      <input type="button" value="Cancel" onclick="window.history.back()">
    </p>
  </form>
  <script>
    setCurrentValues("${employee.department.name}", "${employee.active}");
  </script>
</body>
</html>