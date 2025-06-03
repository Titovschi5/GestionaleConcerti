<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Modifica Utente</title>
</head>
<body>
<h1>Modifica Utente</h1>

<form action="${pageContext.request.contextPath}/modificaUtente" method="post">
  <input type="hidden" name="id" value="${utente.id}" />

  <label for="username">Username:</label>
  <input type="text" id="username" name="username" value="${utente.username}" required /><br/>

  <label for="email">Email:</label>
  <input type="email" id="email" name="email" value="${utente.email}" required /><br/>

  <button type="submit">Salva Modifiche</button>
</form>

<a href="${pageContext.request.contextPath}/dashboardAdmin">Torna alla Dashboard</a>
</body>
</html>