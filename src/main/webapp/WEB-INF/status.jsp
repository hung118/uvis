Status is Fine....
<%
out.println("<BR><BR><B>Memory Used</B>: "+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "K");
out.println("<BR><BR><B>Memory Free</B>: "+ (Runtime.getRuntime().freeMemory() + " (" + (Runtime.getRuntime().freeMemory() / 1024) + "K)"));
out.println("<BR><BR><B>Memory Allowable</B>: "+ (Runtime.getRuntime().maxMemory() / 1024) + "K");
%>