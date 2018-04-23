<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title> Employee full information</title>
</head>

<body>
  <table border=1>
    <thead>
      <tr>
        <th>Employee ID</th>
        <th>Employee name</th>
        <th>Employee active</th>
        <th>Employee department</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td> ${employee.id} </td>
        <td> ${employee.name} </td>
        <td> ${employee.active} </td>
        <td> ${employee.department.name} </td>
      </tr>
    </tbody>
  </table>
</body>
</html>
