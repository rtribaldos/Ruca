<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.model.MediaObject" %>
<%@ page import="com.model.Gallery" %>
<%@ page import="com.model.dao.GaleriaDAO" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) request.getAttribute("user");
  String authURL = (String) request.getAttribute("authURL");
  String uploadURL = (String) request.getAttribute("uploadURL");
  String vivienda = (String) request.getAttribute("vivienda");
  String nomVivienda = (String) request.getAttribute("desc");
  Gallery galeria = (Gallery) request.getAttribute("galeria");
  String antes = (String) request.getAttribute("antes");
  String tieneAntes = (String) request.getAttribute("tieneAntes");
  List<MediaObject> fotos = (List<MediaObject>) request.getAttribute("fotos");
  if(tieneAntes==null) tieneAntes="";
  if(antes != null && !"".equals(antes)) antes=" > ANTES DE REFORMAR";
  else antes="";
  int contador=0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
	<title>Gracia Rubio</title>
	
	<link href='http://fonts.googleapis.com/css?family=Istok+Web:400,700' rel='stylesheet'>
	<link href='http://fonts.googleapis.com/css?family=Yesteryear' rel='stylesheet'>
	<link rel="stylesheet" href="css/style.css" media="screen">
	<link rel="stylesheet" href="css/jquery.lightbox-0.5.css" media="screen" />	
		
		
	<link rel="icon" href="images/home2.png" type="image/x-icon">
	<link rel="shortcut icon" href="images/home2.png" type="image/x-icon" />
	
	<script src="js/jquery-1.7.2.min.js"></script>
	<script src="js/jquery.lightbox-0.5.js"></script>
	<script src="js/script.js"></script>
	<script src="js/superfish.js"></script>
	<script src="js/jquery.responsivemenu.js"></script>
	<script src="js/jquery.flexslider-min.js"></script>
	<script src="js/jquery.easing.1.3.js"></script>
	<script src="js/jquery.ui.totop.js"></script>
	
	 <script type="text/javascript">
    $(function() {
        $('#gallery a').lightBox();
    });
    </script>
	
		
</head>
<body>
	<div class="container_12">
		<div class="block-0">
			<!-- header -->
				<div class="wrapper block-1">
					<div>
						<a href="/"><h1><img src="images/logoT2.png" /></h1></a>
					</div>
				</div>
				
				<div class="menu-border">
					<!-- menu -->
					<nav>
						<ul class="sf-menu">
							<li class="current"><center><a href="#">PROYECTOS</a></center>
								<ul>
									<li><a href="/Ruca?galeria=decoracion"><center>DECORACI&Oacute;N</center></a></li>
									<li><a href="/Ruca?galeria=reformas"><center>REFORMAS</center></a></li>
									<li><a href="/Ruca?galeria=obraNueva"><center>OBRA NUEVA</center></a></li>
									<li><a href="/Ruca?galeria=oficinas"><center>OFICINAS</center></a></li>
								</ul>
							</li>
							<li ><a href="/filosofia.jsp"><center>FILOSOFIA</center></a></li>
							<li ><a href="/contacto.jsp"><center>CONTACTO</center></a></li>
						</ul>
						
						<div class="clear"></div>	
					</nav><!-- end menu -->
				</div>
			
		</div>
			
		<!-- content -->
		<div class="wrapper">
					
							
							<%
							 String urlName="";
							 String cssMiga = "grid_6";
							 String urlNameBack="/Ruca?galeria=reformas";
							 if(!"".equals(antes)){
								 urlNameBack="/?galeria=reformas&nombre=" + vivienda+"&desc="+ nomVivienda ;
								 cssMiga = "grid_12";
							 }
							%>
							<div class="<%=cssMiga%>">
							<h5><b>PROYECTOS > <a href="<%=urlNameBack%>">REFORMAS  > <%=nomVivienda%> <%=antes%></a></b>
							</div>
							<div class="grid_6">
							<%
							 if(!"".equals(tieneAntes) && "".equals(antes)){
								 urlName="/?galeria=reformas&nombre=" + vivienda+"&antes=S&desc="+ nomVivienda ;
							%>
							  <p align="right"><a href="<%=urlName%>" style="color:black"><b>VER ANTES DE REFORMAR</b></a></p>
							<% 
							 }
							%>
							</div>
							</h5>
							
							
						<br>	
						<div class="grid_12b">	
							<br>
							<div id="gallery">
							<%
							if(galeria != null){
								for(MediaObject foto: fotos){ 
									if(vivienda.equals(foto.getTitle())){
										contador++; 
										if((!"".equals(foto.getTextAntes()) && !"".equals(antes))
												|| ("".equals(foto.getTextAntes()) && "".equals(antes))){
										
							%>
											<div class="photoDiv">
												<p><a  href="<%=foto.getURLPath()%>"  title="<%=nomVivienda%>">
												<img src="<%=foto.getURLPath()%>" alt="<%=foto.getTitle()%>"  style="display: block; height: 200px; margin:0px; padding:0px; border: 1px solid white"/></a></p>
											</div>
							
							<%
										}
									}
								}
							}
							%>	
									
						</div>
					</div>
			</div>
	</div>
		
				
	<!-- footer -->
	<div class="container_12">
		<hr>
			<div class="wrapper">
				<div class="grid_4">
				<p style='font:11px/21px "Trebuchet MS", Helvetica, sans-serif;'>C/ Salv&aacute; 8, 5&ordm;-9 - Valencia  963 253 539 / 609 855 196</p>
				</div>
				<div class="grid_4">
					<a href="https://instagram.com/graciarubioestudio"><p align="center"><img src="images/instagram.png" height="26px"  alt="Instagram" ></p></a>
				</div>			
				<div class="grid_4">
					<a href="/upload"><p align="right" style='color: #6f6f71; font:11px/21px "Trebuchet MS", Helvetica, sans-serif;'>&copy; RUCA INTERIORES S.L.</p></a>
				</div>
			</div>
	</div>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-7078796-5']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>	
</body>

<!-- Mirrored from static.livedemo00.template-help.com/wt_38557/index.html by HTTrack Website Copier/3.x [XR&CO'2010], Mon, 09 Apr 2012 19:51:29 GMT -->
</html>
