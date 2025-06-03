<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Modifica Utente</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap">
  <style>
    body {
      font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
      margin: 0;
      padding: 0;
      background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
      color: #212529;
      line-height: 1.6;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }

    header {
      background-color: white;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      padding: 1.5rem 5%;
      position: relative;
      overflow: hidden;
    }

    header::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, #4CAF50, #8BC34A, #4CAF50);
      z-index: 1;
    }

    main {
      flex: 1;
      max-width: 800px;
      width: 90%;
      margin: 2rem auto;
      padding: 2rem;
      background-color: white;
      border-radius: 12px;
      box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
      position: relative;
      overflow: hidden;
    }

    main::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 5px;
      background: linear-gradient(90deg, #4CAF50, #8BC34A);
    }

    h1 {
      margin: 0 0 1.5rem 0;
      font-size: 2rem;
      font-weight: 600;
      color: #212529;
      position: relative;
      display: inline-block;
    }

    h1::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 50px;
      height: 3px;
      background-color: #4CAF50;
      border-radius: 3px;
    }

    form {
      margin-top: 2rem;
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
    }

    .form-group {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    label {
      font-weight: 500;
      color: #495057;
      font-size: 0.95rem;
    }

    input, select {
      padding: 0.8rem 1rem;
      border: 1px solid #ced4da;
      border-radius: 8px;
      font-size: 1rem;
      color: #495057;
      transition: all 0.2s ease;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
    }

    input:focus, select:focus {
      border-color: #4CAF50;
      box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.25);
      outline: none;
    }

    select {
      appearance: none;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%23495057' viewBox='0 0 16 16'%3E%3Cpath d='M8 11.5l-6-6 1.5-1.5L8 8.5l4.5-4.5L14 5.5l-6 6z'/%3E%3C/svg%3E");
      background-repeat: no-repeat;
      background-position: right 1rem center;
      background-size: 16px;
      padding-right: 2.5rem;
    }

    .buttons-container {
      display: flex;
      gap: 1rem;
      margin-top: 1rem;
    }

    button, .back-link {
      padding: 0.8rem 1.5rem;
      border-radius: 8px;
      font-weight: 500;
      font-size: 1rem;
      cursor: pointer;
      transition: all 0.2s ease;
      text-align: center;
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      justify-content: center;
    }

    button {
      background-color: #4CAF50;
      color: white;
      border: none;
      box-shadow: 0 3px 8px rgba(76, 175, 80, 0.25);
    }

    button:hover {
      background-color: #388e3c;
      transform: translateY(-2px);
      box-shadow: 0 5px 12px rgba(76, 175, 80, 0.35);
    }

    .back-link {
      background-color: #f8f9fa;
      color: #495057;
      border: 1px solid #ced4da;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    }

    .back-link:hover {
      background-color: #e9ecef;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    /* Animation */
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(10px); }
      to { opacity: 1; transform: translateY(0); }
    }

    main {
      animation: fadeIn 0.3s ease-out forwards;
    }

    /* Validation styles */
    input:invalid {
      border-color: #dc3545;
    }

    input:invalid:focus {
      box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.25);
    }

    .form-error {
      color: #dc3545;
      font-size: 0.85rem;
      margin-top: 0.25rem;
    }

    footer {
      text-align: center;
      padding: 1.5rem;
      background-color: white;
      border-top: 1px solid #e9ecef;
      color: #6c757d;
      margin-top: auto;
    }

    footer p {
      margin: 0;
      font-size: 0.9rem;
    }

    /* Responsive */
    @media (max-width: 768px) {
      main {
        padding: 1.5rem;
      }

      .buttons-container {
        flex-direction: column;
      }

      button, .back-link {
        width: 100%;
      }
    }
  </style>
</head>
<body>
<header>
  <h1>Gestione Utenti</h1>
</header>

<main>
  <h1>Modifica Utente</h1>

  <c:if test="${not empty msg}">
    <div class="form-error">${msg}</div>
  </c:if>

  <form action="${pageContext.request.contextPath}/modificaUtente" method="post">
    <input type="hidden" name="id" value="${utente.id}" />

    <div class="form-group">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" value="${utente.username}" required />
    </div>

    <div class="form-group">
      <label for="email">Email:</label>
      <input type="email" id="email" name="email" value="${utente.email}" required />
    </div>

    <div class="form-group">
      <label for="ruolo">Ruolo:</label>
      <select id="ruolo" name="ruolo" required>
        <option value="utente" ${utente.ruolo == 'utente' ? 'selected' : ''}>Utente</option>
        <option value="admin" ${utente.ruolo == 'admin' ? 'selected' : ''}>Admin</option>
      </select>
    </div>

    <div class="buttons-container">
      <button type="submit">Salva Modifiche</button>
      <a href="${pageContext.request.contextPath}/dashboard" class="back-link">Torna alla Dashboard</a>
    </div>
  </form>
</main>

<footer>
  <p>&copy; Tito Catalano, studente di Ingegneria Digitale</p>
</footer>
</body>
</html>