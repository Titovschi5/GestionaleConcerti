<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard Organizzatore</title>
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
      display: flex;
      justify-content: space-between;
      align-items: center;
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

    h1 {
      margin: 0;
      font-size: 1.8rem;
      font-weight: 600;
      color: #212529;
    }

    h2 {
      color: #212529;
      font-size: 1.5rem;
      margin: 2rem 0 1.5rem;
      font-weight: 600;
      position: relative;
      padding-left: 1rem;
    }

    h2::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      height: 100%;
      width: 4px;
      border-radius: 2px;
      background-color: #4CAF50;
    }

    nav ul {
      list-style-type: none;
      padding: 0;
      display: flex;
      gap: 0.8rem;
      margin: 0;
    }

    nav ul li a {
      display: inline-block;
      text-decoration: none;
      padding: 0.5rem 1rem;
      background-color: #4CAF50;
      color: white;
      border-radius: 6px;
      font-weight: 500;
      transition: all 0.2s ease;
      box-shadow: 0 2px 8px rgba(76, 175, 80, 0.25);
    }

    nav ul li a:hover {
      background-color: #388e3c;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(76, 175, 80, 0.35);
    }

    main {
      flex: 1;
      max-width: 1200px;
      width: 90%;
      margin: 0 auto;
      padding: 2rem 0;
    }

    .debug-info {
      background-color: #f8f9fa;
      border: 1px solid #ddd;
      padding: 10px;
      margin-bottom: 20px;
      font-size: 12px;
      color: #666;
      border-radius: 6px;
    }

    .eventi-container {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1.5rem;
      margin-top: 1.5rem;
    }

    .evento-card {
      background-color: white;
      border-radius: 10px;
      padding: 1.5rem;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
      transition: transform 0.2s, box-shadow 0.2s;
      position: relative;
      overflow: hidden;
    }

    .evento-card::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 3px;
      background: linear-gradient(90deg, #4CAF50, #8BC34A);
    }

    .evento-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
    }

    table {
      width: 100%;
      border-collapse: separate;
      border-spacing: 0;
      background-color: white;
      border-radius: 10px;
      overflow: hidden;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
      margin-bottom: 2rem;
    }

    th, td {
      padding: 1rem;
      text-align: left;
    }

    th {
      background-color: #f8f9fa;
      font-weight: 600;
      color: #495057;
      text-transform: uppercase;
      font-size: 0.75rem;
      letter-spacing: 0.5px;
      border-bottom: 1px solid #e9ecef;
    }

    td {
      border-bottom: 1px solid #e9ecef;
      color: #495057;
      font-size: 0.95rem;
    }

    td strong {
      font-weight: 600;
      color: #212529;
    }

    tr:last-child td {
      border-bottom: none;
    }

    tr:hover {
      background-color: rgba(236, 240, 241, 0.5);
    }

    td ul {
      list-style-type: none;
      padding: 0;
      margin: 0;
    }

    td ul li {
      padding: 0.25rem 0;
    }

    .btn-action, .btn-iscrizione {
      background-color: #4CAF50;
      color: white;
      border: none;
      padding: 0.6rem 1rem;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 500;
      transition: all 0.2s ease;
      font-size: 0.85rem;
      box-shadow: 0 2px 5px rgba(76, 175, 80, 0.2);
    }

    .btn-action:hover, .btn-iscrizione:hover {
      background-color: #388e3c;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(76, 175, 80, 0.3);
    }

    .btn-action[style*="background-color: #d9534f"] {
      background-color: #dc3545 !important;
      box-shadow: 0 2px 5px rgba(220, 53, 69, 0.2);
    }

    .btn-action[style*="background-color: #d9534f"]:hover {
      background-color: #c82333 !important;
      box-shadow: 0 4px 8px rgba(220, 53, 69, 0.3);
    }

    .action-buttons {
      display: flex;
      gap: 0.5rem;
    }

    .alert {
      padding: 1rem 1.5rem;
      margin-bottom: 1.5rem;
      border-radius: 6px;
      font-weight: 500;
      animation: fadeIn 0.3s ease-out forwards;
    }

    .alert-success {
      color: #155724;
      background-color: #d4edda;
      border: 1px solid #c3e6cb;
    }

    .alert-danger {
      color: #721c24;
      background-color: #f8d7da;
      border: 1px solid #f5c6cb;
    }

    section.crea-evento {
      margin-top: 2rem;
      display: flex;
      justify-content: flex-end;
    }

    section.crea-evento .btn-action {
      background-color: #4CAF50;
      padding: 0.8rem 1.5rem;
      font-size: 1rem;
      text-transform: uppercase;
      letter-spacing: 0.5px;
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

    /* Empty state */
    section p:first-of-type:not(.descrizione) {
      background-color: white;
      padding: 1.5rem;
      text-align: center;
      border-radius: 10px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
      color: #6c757d;
    }

    /* Animation */
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(10px); }
      to { opacity: 1; transform: translateY(0); }
    }

    tr {
      animation: fadeIn 0.3s ease-out forwards;
    }

    tr:nth-child(2) { animation-delay: 0.05s; }
    tr:nth-child(3) { animation-delay: 0.1s; }
    tr:nth-child(4) { animation-delay: 0.15s; }
    tr:nth-child(5) { animation-delay: 0.2s; }

    /* Responsive design */
    @media (max-width: 992px) {
      table {
        display: block;
        overflow-x: auto;
      }
    }

    @media (max-width: 768px) {
      header {
        flex-direction: column;
        align-items: flex-start;
      }

      nav ul {
        margin-top: 1rem;
      }

      .action-buttons {
        flex-direction: column;
      }
    }
  </style>
</head>
<body>
<header>
  <h1>Benvenuto, ${user.username}!</h1>
  <nav>
    <ul>
      <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
    </ul>
  </nav>
</header>

<main>
  <!-- Success/Error Messages -->
  <c:if test="${not empty param.success}">
    <div class="alert alert-success">
      Operazione completata con successo!
    </div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger">
        ${param.error}
    </div>
  </c:if>

  <section class="eventi-organizzati">
    <h2>I tuoi eventi</h2>
    <c:choose>
      <c:when test="${empty listaEventi}">
        <p>Non hai ancora creato alcun evento.</p>
      </c:when>
      <c:otherwise>
        <table>
          <thead>
          <tr>
            <th>Nome</th>
            <th>Data</th>
            <th>Ora</th>
            <th>Luogo</th>
            <th>Categoria</th>
            <th>Capacità</th>
            <th>Username iscritti</th>
            <th>Azioni</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="evento" items="${listaEventi}">
            <tr>
              <td>
                <strong>${evento.nome}</strong>
              </td>
              <td>
                  ${evento.data}
              </td>
              <td>
                  ${evento.ora}
              </td>
              <td>
                  ${evento.luogo}
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty evento.categoria}">
                    ${evento.categoria.nome}
                  </c:when>
                  <c:otherwise>
                    -
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                  ${evento.capacita}
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty mappaPartecipanti[evento.id]}">
                    <ul>
                      <c:forEach var="partecipante" items="${mappaPartecipanti[evento.id]}">
                        <li>${partecipante.username}</li>
                      </c:forEach>
                    </ul>
                  </c:when>
                  <c:otherwise>
                    Nessun partecipante
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <div class="action-buttons">
                  <form action="${pageContext.request.contextPath}/modificaEvento" method="get" style="margin:0; padding:0; display:inline;">
                    <input type="hidden" name="id" value="${evento.id}" />
                    <button type="submit" class="btn-action">Modifica</button>
                  </form>
                  <form action="${pageContext.request.contextPath}/eliminaEvento"
                        method="post"
                        onsubmit="return confirm('Sei sicuro di voler eliminare questo evento?');"
                        style="margin:0; padding:0; display:inline;">
                    <input type="hidden" name="eventoId" value="${evento.id}" />
                    <button type="submit" class="btn-action" style="background-color: #d9534f;">Elimina</button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>
  </section>

  <section>
    <h2>Gestione Utenti</h2>
    <c:if test="${not empty listaUtenti}">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>Ruolo</th>
          <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="utente" items="${listaUtenti}">
          <tr>
            <td>${utente.id}</td>
            <td>${utente.username}</td>
            <td>${utente.email}</td>
            <td>${utente.ruolo}</td>
            <td>
              <form action="${pageContext.request.contextPath}/modificaUtente" method="get" style="margin: 0;">
                <input type="hidden" name="id" value="${utente.id}" />
                <button type="submit" class="btn-action">Modifica</button>
              </form>
              <form action="${pageContext.request.contextPath}/eliminaUtente" method="post" style="margin: 0; margin-top: 5px;" onsubmit="return confirm('Sei sicuro di voler eliminare questo utente?');">
                <input type="hidden" name="id" value="${utente.id}" />
                <button type="submit" class="btn-action" style="background-color: #dc3545;">Elimina</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:if>
    <c:if test="${empty listaUtenti}">
      <p>Nessun utente trovato.</p>
    </c:if>
  </section>

  <section class="crea-evento">
    <form action="${pageContext.request.contextPath}/creaEvento" method="get">
      <button type="submit" class="btn-action">Crea un nuovo evento</button>
    </form>
  </section>
</main>

<footer>
  <p>&copy; Tito Catalano, studente di Ingegneria Digitale</p>
</footer>
</body>
</html>