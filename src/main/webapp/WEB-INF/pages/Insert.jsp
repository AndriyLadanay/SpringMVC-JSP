<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
  <title>Edit Employee</title>
</head>
<body>
  <form method="post">
    <p> empName <input type="text" name="name"> </p>
    <p> empActive <input type="checkbox" name="active"> </p>
    <p> empDepartment
      <select name="department">
        <c:forEach items="${departments}" var="department">
          <option value="<c:out value="${department.ID"/>">
            <c:out value="${department.name}" />
          </option>
        </c:forEach>
      </select>
    </p>
    <p> <input type="submit"  value="Save"> <input type="reset" value="Cancel"> </p>
  </form>
</body>
</html>
