<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.model.MediaObject" %>
<%@ page import="com.model.Gallery" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) request.getAttribute("user");
  String authURL = (String) request.getAttribute("authURL");
  String uploadURL = (String) request.getAttribute("uploadURL");
  Gallery gallery = (Gallery) request.getAttribute("galeria");
  String ordenar = (String) request.getAttribute("ordenar");
  int contador=0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<title>Ruca Interiores</title>
	
	<link rel="stylesheet" href="css/theme.css" media="screen">
	<link rel="stylesheet" href="css/styleAdmin.css" media="screen" />	
</head>
<body>
<div id="container">
    	<div id="header">
        	<h2>Administraci&oacute;n Ruca Interiores <br><BR> <%=gallery.getName().toUpperCase()%></h2>
   
        <div id="wrapper">
           <br><br><br><br>
            <div id="sidebar">
  				<ul>
                	<li><h3><a href="/" class="house">P&aacute;gina Principal</a></h3></li>
                    <li><h3><a href="/upload?galeria=obraNueva" class="folder_table">Obras Nuevas</a></h3></li>
                    <li><h3><a href="/upload?galeria=reformas" class="manage">Reformas</a></h3></li>
                  	<li><h3><a href="/upload?galeria=decoracion" class="user">Decoraci&oacute;n</a></h3></li>
                  	<li><h3><a href="/upload?galeria=oficinas" class="user">Oficinas</a></h3></li>
				</ul>       
          </div>
         </div> 
         <div id="content">
          	<div id="box">
                	<h3><%=gallery.getName().toUpperCase()%></h3>
                	<table width="100%">
						<thead>
							<tr>
                            	<th width="40px"><a href="#">Orden<img src="img/icons/arrow_down_mini.gif" width="16" height="16" align="absmiddle" /></a></th>
                            	<th><a href="#">Nombre</a></th>
                            	<th><a href="#">Descripci&oacute;n</a></th>
                            	<th><a href="#">Foto principal</a></th>
								<th><a href="#">Subir / Bajar</a></th>
								<th width="60px"><a href="#">Borrar</a></th>
                            </tr>
						</thead>
						<tbody>
							<%
							List<MediaObject> photosPrincipales = gallery.getPrincipales();
							if (gallery != null && photosPrincipales != null && photosPrincipales.size() > 0) {
								Integer orden = 1;
								for (int i = 0; i < photosPrincipales.size(); i++) {
									MediaObject	foto = photosPrincipales.get(i);
									if (foto != null) {
										orden = foto.getOrden() + 1;
										String urlSortUp = "/upload?galeria=" + gallery.getName() + "&subirOrden=" + foto.getTitle()
											+ "&ordenActual=" + foto.getOrderInGallery() + "&principal=true";
										String urlSortDown = "/upload?galeria=" + gallery.getName() + "&bajarOrden=" + foto.getTitle()
											+ "&ordenActual=" + foto.getOrderInGallery() + "&principal=true";
										String urlBorrado="/upload?galeria=" + gallery.getName() + "&borrar=" + foto.getFilename();
										String urlDetalle="/upload?galeria=" + gallery.getName() + "&detalle=" + foto.getTitle();
										
							%>
							<tr>
                            	<td class="a-center"><%=++contador%></td>
                            	<td><a href="<%=urlDetalle%>"><%=foto.getTitle() %></a></td>
                            	<td><a href="<%=urlDetalle%>"><%=foto.getDescription() %></a></td>
                            	<td><a href="#"><%=foto.getFilename()%></a></td>
                            	
                            	<td class="a-center">
									<a href="<%=urlSortUp%>">
										<img src="img/icons/arrow_up_mini.gif" width="16" height="16" align="absmiddle" />
									</a>
									<a href="<%=urlSortDown%>">
										<img src="img/icons/arrow_down_mini.gif" width="16" height="16" align="absmiddle" />
									</a>									
								</td>
								
                            	<td class="a-center">
		                    		<a href="<%=urlBorrado%>"><img src="img/icons/user_delete.png" title="Delete user" width="16" height="16" /></a>
		                    	</td>
                            </tr>
							<%
										
									}
								}
							}
							%>
						</tbody>
					</table>
           </div>
           
           <div id="box">
                	<h3 id="adduser"></h3>
                    <form id="form" action="<%= uploadURL%>" method="POST" enctype="multipart/form-data">
                      <fieldset id="personal">
                        <legend> * Nueva</legend>
                        <label for="lastname">Nombre: </label> 
                        <input name="title" id="title" type="text" tabindex="1" size="30" />
                        <br />
                        <label for="firstname">Descripci&oacute;n: </label>
                        <input name="description" id="description" type="text" tabindex="2" size="60"/>
                        <br />
                        <label for="email">Foto Principal: </label>
                        <input type="file" name="file">
                        <br />
                        <input type="hidden" name="principal" id="yes" value="yes" />
                        <input type="hidden" name="op" value="<%=gallery.getName()%>" />
                     </fieldset>
                     
                     
                     <div align="center">
                      <input id="button1" type="submit" value="Guardar" /> 
                      <input id="button2" type="reset" />
                     </div>
                    </form>

                </div>
          </div>  
     	 </div>
     	 
        <div id="footer">
       
		</div>
</body>
</html>