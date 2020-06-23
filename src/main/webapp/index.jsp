<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.model.MediaObject" %>
<%@ page import="com.model.Gallery" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) request.getAttribute("user");
  String authURL = (String) request.getAttribute("authURL");
  String uploadURL = (String) request.getAttribute("uploadURL");
  Gallery gallery = (Gallery) request.getAttribute("galeria");
  int contador=0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="author" content="Raul Tribaldos"/>
<meta name="description" content="Gracia Rubio, interiorista"/>
<meta name="keywords" content="decoracion, reformas, gracia rubio, ruca, rucainteriores, interiorismo, interiorista, obra, oficinas"/>
<meta name="language" content="spanish"/>
<title>Gracia Rubio</title>
	<style type="text/css">
		@import "css/style.css";
	</style>
	<script src="js/jquery.min.js"></script>
	<script src="js/script.js"></script>
	<script src="js/superfish.js"></script>
	<script src="js/jquery.responsivemenu.js"></script>
	<script src="js/jquery.flexslider-min.js"></script>
	<script src="js/jquery.easing.1.3.js"></script>
	<script src="js/jquery.ui.totop.js"></script>
	<script type="text/javascript" src="js/modal.popup.js"></script>
	
	
	<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
        
		//Change these values to style your modal popup
		var align = 'center';									//Valid values; left, right, center
		var top = 170; 											//Use an integer (in pixels)
		var width = 680; 										//Use an integer (in pixels)
		var width2 = 750;
		var padding = 10;										//Use an integer (in pixels)
		var backgroundColor = '#f7f7f3'; 						//Use any hex code
		var source = '/about'; 								//Refer to any page on your server, external pages are not valid e.g. http://www.google.co.uk
		var borderColor = '#f7f7f3'; 							//Use any hex code
		var borderWeight = 0; 									//Use an integer (in pixels)
		var borderRadius = 5; 									//Use an integer (in pixels)
		var fadeOutTime = 300; 									//Use any integer, 0 = no fade
		var disableColor = '#666666'; 							//Use any hex code
		var disableOpacity = 40; 								//Valid range 0-100
		var loadingImage = 'images/loading.gif';		//Use relative path from this page
			
		//This method initialises the modal popup
        $(".modal").click(function() {
            modalPopup(align, top, width2, padding, disableColor, disableOpacity, backgroundColor, borderColor, borderWeight, borderRadius, fadeOutTime, source, loadingImage);
        });
		
        $(".ejemplos").click(function() {
            modalPopup(align, top, width, padding, disableColor, disableOpacity, backgroundColor, borderColor, borderWeight, borderRadius, fadeOutTime, '/about?op=reforma', loadingImage);
        });
		
		//This method hides the popup when the escape key is pressed
		$(document).keyup(function(e) {
			if (e.keyCode == 27) {
				closePopup(fadeOutTime);
			}
		});
		
    });
	</script>
	
	<style type="text/css" media="screen">
		#large_box {width:800; height:600;}
	</style>
	<script>
		$(function(){
			$('.flexslider').flexslider({
				animation: "fade",
				slideshow: true,
				slideshowSpeed: 7000,
				animationDuration: 600,
				prevText: "Previous",
				nextText: "Next",
				controlNav: false,
			});
		})
	</script>
	
	
</head>
<body id="page1">
	<div class="container_12">
		<div class="block-0">
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
		 		<BR>
				<div class="grid_12">
					<div class="flexslider ident-bot-2">
						<ul class="slides">
							<%
							if(gallery != null){
								for(MediaObject foto: gallery.getFotos()){
							%>
							
							<li>
								<img src="<%=foto.getURLPath() %>" alt="">
							</li>
							<%
								}
							}else{
							%>
							<li>
								<img src="images/a1.jpg" alt="" height="360px" width="960px">
							</li>
							<%
							}
							%>
							
						</ul>
						
						<br><BR>
						<hr>
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
</html>
