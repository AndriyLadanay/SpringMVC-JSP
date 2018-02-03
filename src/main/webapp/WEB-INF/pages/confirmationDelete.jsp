<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Choice</title>
</head>

<body>
  <form action="/employees/delete" method="post">
    <p> <input type="hidden" name="id"  value="<c:out value="${id}"/>"/> </p>
    <p> Do you really want to delete this record? </p>
    <p>
      <input type="submit" value="Yes"/>
      <input type="button" value="No" onclick="window.history.back()">
    </p>
  </form>
</body>
</html>
