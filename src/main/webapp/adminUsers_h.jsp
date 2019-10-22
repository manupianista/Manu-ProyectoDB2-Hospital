

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!--head-->
    <jsp:include page="partials/_head.jsp">
        <jsp:param name="title" value="Modificar Usuario" />
    </jsp:include>
    
    <!--navbar logged-->
    <%@ include file="partials/_headerLogged.jsp"%>
    
    <!--Verify if the user has access-->
    <%@ include file="partials/_getInfo.jsp"%>
    <%
        if(rol.equals("1")){
        }
        else{
            response.sendRedirect("home_h.jsp");
        }
    %>
    
    <body>
        <div class="grid-container">
            <div class="grid-x align-center-middle">
                <div class="cell small-10 medium-8">
                    <form class="form" action="http://192.168.69.4:8888/proyectoDB2-Hospital1/restU/usuarios/addUsuarios" method="post">
                        <!--<h4>Modificar <%=request.getParameter("username")%></h4>-->
                         <label>username: 
                            <input id="username" type="text" placeholder="Coloque el nuevo username del usuario" name="username" required>
                        </label>
                        <label>contrasenia 
                            <input id="contra" type="text" placeholder="Coloque el nuevo contrasenia del usuario" name="passw" required>
                        </label>
                        <label>Nombre:
                            <input type="number" value="<%=request.getParameter("uId")%>" style="display:none" name="uId">
                            <input id="nombreU" type="text" placeholder="Coloque el nuevo nombre del usuario" name="name" required>
                        </label>
                        <label>Apellido: 
                            <input id="apellidoU" type="text" placeholder="Coloque el nuevo apellido del usuario" name="lastName" required>
                        </label>
                       
                        <label>Telefono: 
                            <input id="tel" type="number" placeholder="Coloque el nuevo telefono del usuario" name="tel" required>
                        </label>
                        <label>Especialidad: 
                            <select id="especialidadData" name="usSpecial">
                            </select>
                        </label>
                        <label>Tipo de usuario:
                            <select id="typeData" name="usType">
                            </select>
                        </label> 
                       
                        <br>
                        
                            
                        <input type="submit" class="cell button medium-8" value="Modificar">
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script src="js/especialidadInf.js"></script>
    <script src="js/typeInf.js"></script>
    <script src="js/userModInf.js"></script>
</html>































































