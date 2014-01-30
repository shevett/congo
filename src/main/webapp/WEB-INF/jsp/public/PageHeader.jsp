	<div class='box-green'>
 		<div class='boxtop-green'><div></div></div>
  			<div class='boxcontent-green'>
		<div class="dates">
			Start: <fmt:formatDate dateStyle="long" timeStyle="short" value="${currentConvention.conStart}"/><br>
			End: <fmt:formatDate dateStyle="long" timeStyle="short" value="${currentConvention.conEnd}"/>
		</div>
		
		<div class="title">
		 	<a href="${sessionScope.currentConvention.conWebsite }">${sessionScope.currentConvention.conName }</a>
		</div>
	
		<div class="comment">
			${currentConvention.conComment }
		</div>
	
		<div class="description">
			${currentConvention.conLocation}
		</div>
  			</div>
 		<div class='boxbottom-green'><div></div></div>
	</div>
