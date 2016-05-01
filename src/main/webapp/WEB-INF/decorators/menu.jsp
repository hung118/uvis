<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>

<div id="yui-main" style="padding-left:40px;background-color:#CE0000;">
	<!-- start: stack grids here -->

	<div id="mainmenu" class="yuimenubar yuimenubarnav" style="border-right: none">
		<div class="bd">
			<ul class="first-of-type">
				<li class="yuimenubaritem">
					<s:a namespace="/" action="sessionInit">Home</s:a>
				</li>
				<logic:notPresent role="Read Only,VSO">
					<li class="yuimenubaritem">
						<s:a namespace="/" action="Dashboard">Dashboard</s:a>
					</li>
					<li class="yuimenubaritem">
						<a href="#">Reports</a>
						<div class="yuimenu">
							<div class="bd">
								<ul>
									<li class="yuimenuitem">
										<s:url id="scoreCardReport" namespace="/report" action="scoreCardReport" />
										<s:a href="%{scoreCardReport}">Score Card</s:a>
									</li>
									<li class="yuimenuitem">
										<s:url id="zipMailingList" namespace="/report" action="zipMailingListReport" />
										<s:a href="%{zipMailingList}">Mailing List by Zip Codes</s:a>
									</li>
									<li class="yuimenuitem">
										<s:url id="emailList" namespace="/report" action="emailListReport" />
										<s:a href="%{emailList}"><s:text name="report.emailList" /></s:a>
									</li>
									<li class="yuimenuitem">
										<s:url id="emailRandom" namespace="/report" action="emailRandomReport" />
										<s:a href="%{emailRandom}"><s:text name="report.emailRandom" /></s:a>
									</li>
									<li class="yuimenuitem">
										&nbsp; &nbsp; ----------------------------
									</li>
									<li class="yuimenuitem">
										<s:url id="userReport" namespace="/report" action="listReport"><s:param name="type" value="%{'1'}" /></s:url>
										<s:a href="%{userReport}">User Reports</s:a>
									</li>
									<li class="yuimenuitem">
										<s:url id="systemReport" namespace="/report" action="listReport"><s:param name="type" value="%{'2'}" /></s:url>
										<s:a href="%{systemReport}">System Reports</s:a>
									</li>
									<logic:present role="Super User">
									<li class="yuimenuitem">
										<s:url id="allReport" namespace="/report" action="listReport"><s:param name="type" value="%{'3'}" /></s:url>
										<s:a href="%{allReport}">All Reports</s:a>
									</li>
									</logic:present>
								</ul>
							</div>
						</div>
					</li>				
				</logic:notPresent>
				<logic:present role="VSO">
					<li class="yuimenubaritem">
						<s:a namespace="/" action="displaySearchVeteranSearch?vaCurrent=1">Search</s:a>
					</li>				
				</logic:present>
				<logic:notPresent role="VSO">
					<li class="yuimenubaritem">
						<a href="#">Search</a>
						<div class="yuimenu">
							<div class="bd">
								<ul>
									<li class="yuimenuitem">
										<s:a namespace="/" action="displaySearchVeteranSearch?standard=1">Standard Search</s:a>
									</li>
									<li class="yuimenuitem">
										<s:a namespace="/" action="displaySearchVeteranSearch?adhoc=1">Ad-Hoc Search</s:a>
									</li>
								</ul>
							</div>
						</div>
					</li>				
				</logic:notPresent>
				<logic:notPresent role="Read Only,VSO">
					<li class="yuimenubaritem">
						<a href="#">New Entry</a>
						<div class="yuimenu">
							<div class="bd">
								<ul>
									<li class="yuimenuitem">
										<s:a namespace="/" action="ShortForm">Short Form</s:a>
									</li>
									<li class="yuimenuitem">
										<s:a namespace="/" action="newEntryVeteran">Long Form</s:a>
									</li>
								</ul>
							</div>
						</div>
					</li>				
				</logic:notPresent>

				<logic:present role="Admin,Super User">
				<li class="yuimenubaritem">
					<a href="#">Administration</a>
					<div class="yuimenu">
						<div class="bd">
							<ul>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayUser">Users</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayEthnicity">Ethnicities</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayServiceEra">Service Eras</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayServiceBranch">Service Branches</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayDischargeType">Discharge Types</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayBenefitType">Benefit Types</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayDocType">Doc Types</s:a>
								</li>
								<logic:present role="Super User">
									<li class="yuimenuitem">
										<s:a namespace="/" action="displayRecordType">Record Types</s:a>
									</li>
								</logic:present>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayRelation">Relationship to Veteran</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayCombatZone">Combat Zones</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayDecorationMedal">Decorations and Medals</s:a>
								</li>
								<li class="yuimenuitem">
									<s:a namespace="/" action="displayRural">Rural Crosswalk</s:a>
								</li>
							</ul>
						</div>
					</div>

				</li>
				<logic:present role="Super User">
					<li class="yuimenubaritem">
						<a href="#">Data Imports</a>
						<div class="yuimenu">
							<div class="bd">
								<ul>
									<li class="yuimenuitem">
										<s:a namespace="/data" action="displayDldDriverLicense">Driver License SFTP</s:a>
									</li>
									<li class="yuimenuitem">
										<s:a namespace="/data" action="displayDldFileDriverLicenseFile">Driver License File Upload</s:a>
									</li>
									<li class="yuimenuitem">
										<s:a namespace="/data" action="displayDwsDWS">DWS SFTP</s:a>
									</li>
									<li class="yuimenuitem">
										<s:a namespace="/data" action="displayDwsFileDWSFile">DWS File Upload</s:a>
									</li>
									<li class="yuimenuitem">
										<s:a namespace="/data" action="displayRegistration">Online Registration Import</s:a>
									</li>
								</ul>
							</div>
						</div>
					</li>
				</logic:present>
				</logic:present>
				
			</ul>
		</div>
	</div>
	
	<!-- end: stack grids here -->
</div>