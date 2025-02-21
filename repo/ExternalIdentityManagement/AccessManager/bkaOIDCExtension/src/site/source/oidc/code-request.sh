https://sso.cinnamonstar.net/oidc?scope=openid+ZIMP.Main&response_type=code&client_id=zimp&redirect_uri=http%3A%2F%2Flocalhost%3A8710%2Flogin&state=&domain=SecureDomain2

Request
-------
GET HTTP/1.1 /oidc 302 Found
https://sso.cinnamonstar.net/oidc?scope=openid+ZIMP.Main&response_type=code&client_id=zimp&redirect_uri=http%3A%2F%2Flocalhost%3A8710%2Flogin&state=&domain=SecureDomain2
Host sso.cinnamonstar.net
Pragma: no-cache
Cache-Control: no-cache
Accept: application/json


Response
-------
HTTP/1.1 /login 404 Not Found
http://localhost:8710/login?code=dUYrTko5ek1ONzhiT2hFd1BCVWUvQT09fnpQd2dDQTVPM0pVTFovV0doNUcybjYwUmQyMXdLM3Rud0dyczBtOE9xMEZxQW5VTDdoOWxBTnRiK2ZPeUJONTFYRFVxSWtFQUZSOGhISFBLNVZzTGFkN3N1dHBVQkpzUXY0dEViOW8rZ3oranJpMEowNVZuSTR6ZWJpa2pldnUxZVZYL2lzMXJmcVFTc2p4UjhyUmpjWmVkK01ycTNrb1UweCsrb3BMQjFndm14REU5UlRkWmxXKzRPNVF3cmFaaWcvd3FmWXE5TEFXUEpnZ3FBN1hFa2lFM3hoZFpXcjVsUmdXOXRMSGlHTEIyamdvZTZMYzhBTThFVVVXUm5Cd2hUQ096MTdXaHBCYWRoRjQ0aTY1czd2TXh1SWM2amVabEwrWkQrZUxpdjFUcFZFMjZ5T3hKOU5xWk9DaGNWL1FaMlBac2ZRbkxOMHg3bGtuUjhUTnJJaXRYU2RrRExmbHQ2VnhmcmxPMkRNVG5uTlloNENpZ1hGYWh0b3RyNXJsMUJGSmZqQ0Urb1BTOGJvMitCWUNSc01meStZU1JRWkhtd2NwSDRLN0ZhOTBnSy9uUTdkMzFwbnpVaklsVHp6ZDNJbVdhQzc3NXhTbmFvMmdrNWY2WlVVSXg1Mmg0ZEYvMmlES0dHZUxRM0xkYTdyMkJCdHlIdS9RU1VDVXJMbkQwZkFwQys2NmxlOGNzUzd3TXh2T2hnaGN1SCtNQkxBOENicVk1RGlZaG9TRXREcEpDYndndHBOQmt2ditiRjRFNA==
Host localhost:8710
Pragma: no-cache
Cache-Control: no-cache

Request
-------
POST /oauth2/rest/token HTTP/1.1
Host: sso.cinnamonstar.net
Accept: application/json
Content-Type: application/x-www-form-urlencoded
Authorization: Basic emltcDp3bWthaDFtZGto
x-oauth-identity-domain-name: SecureDomain2

grant_type=AUTHORIZATION_CODE&redirect_uri=http%3A%2F%2Flocalhost%3A8710%2Flogin&code=dUYrTko5ek1ONzhiT2hFd1BCVWUvQT09fnpQd2dDQTVPM0pVTFovV0doNUcybjYwUmQyMXdLM3Rud0dyczBtOE9xMEZxQW5VTDdoOWxBTnRiK2ZPeUJONTFYRFVxSWtFQUZSOGhISFBLNVZzTGFkN3N1dHBVQkpzUXY0dEViOW8rZ3oranJpMEowNVZuSTR6ZWJpa2pldnUxZVZYL2lzMXJmcVFTc2p4UjhyUmpjWmVkK01ycTNrb1UweCsrb3BMQjFndm14REU5UlRkWmxXKzRPNVF3cmFaaWcvd3FmWXE5TEFXUEpnZ3FBN1hFa2lFM3hoZFpXcjVsUmdXOXRMSGlHTEIyamdvZTZMYzhBTThFVVVXUm5Cd2hUQ096MTdXaHBCYWRoRjQ0aTY1czd2TXh1SWM2amVabEwrWkQrZUxpdjFUcFZFMjZ5T3hKOU5xWk9DaGNWL1FaMlBac2ZRbkxOMHg3bGtuUjhUTnJJaXRYU2RrRExmbHQ2VnhmcmxPMkRNVG5uTlloNENpZ1hGYWh0b3RyNXJsMUJGSmZqQ0Urb1BTOGJvMitCWUNSc01meStZU1JRWkhtd2NwSDRLN0ZhOTBnSy9uUTdkMzFwbnpVaklsVHp6ZDNJbVdhQzc3NXhTbmFvMmdrNWY2WlVVSXg1Mmg0ZEYvMmlES0dHZUxRM0xkYTdyMkJCdHlIdS9RU1VDVXJMbkQwZkFwQys2NmxlOGNzUzd3TXh2T2hnaGN1SCtNQkxBOENicVk1RGlZaG9TRXREcEpDYndndHBOQmt2ditiRjRFNA==

Response
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: no-store
Pragma: no-cache
{ "token_type"    : "Bearer"
, "id_token"      : "eyJraWQiOiJTZWN1cmVEb21haW4yIiwieDV0IjoiZUZ6RUJjZF9Dd0lfakZZNW1yZVRHdThrYVpRIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL3Nzby5jaW5uYW1vbnN0YXIubmV0OjQ0My9vYXV0aDIiLCJzdWIiOiJhbjQ3MTExMjMiLCJhdWQiOlsiaHR0cHM6Ly9zc28uY2lubmFtb25zdGFyLm5ldDo0NDMvb2F1dGgyIiwiWklNUCIsInppbXAiXSwiZXhwIjoxNzI5NDYzNTU2LCJpYXQiOjE3Mjk0NTYzNTYsImp0aSI6Im5weGJfZWNYNHhzNVMyTjFnbWMtY0EiLCJhdF9oYXNoIjoickVBYy03c0tlYmtDb2F3aVNEX3VxdyIsImF6cCI6InppbXAiLCJhY3IiOiIyIiwic2lkIjoiYy80N0l0TjF5Y2Y1YUxxanpoNkZQdz09fjNDRU9LVnJDcGZ5SHNoa3NEYzRrNW40dFJvSGsybGRseU1VZkZhS0RSTm1jdlZMU3ZxSlYwN2p1bEZDeGkwS3NLRDRoZkZIUmJvQ0U2ZExqSGkyMGUzTEVheHJDY2RBYzZxKzQ3NVN5R1p2V05ZbVRRL1NYT2dlamlNMkJCelVpIiwiYXV0aF90aW1lIjoxNzI5NDU2MTYxLCJhbXIiOlsicHdkIl0sImRpdmlzaW9uIjoiQU5fMSIsInAyMF91aWQiOiJULTM2LTE1LTE1LTEwMS1BTjQ3MTExMjMiLCJpZHAiOiJBTiIsInBob25lX251bWJlciI6Iis0OSAoMCkxNzcgNTk0OCA0MzciLCJzZXNzaW9uSWQiOiIxZDgwYzMxMC0zZGQzLTRkYzMtOGUyMi0wOTNmZjA2YzNiOTh8RWw2elB6WkFCSTNJL2hvUFk3MFNibnFTeGVRUnAzajdXOU95QXB6eUp3VT0iLCJnaXZlbl9uYW1lIjoiQWxmb25zIiwiZmF4X251bWJlciI6Iis0OSAxNzcgNTk0OCAyMjIiLCJmYW1pbHlfbmFtZSI6IlppdHRlcmJhY2tlIiwiZW1haWwiOiJ3ZWdlbi1vaWRjLW1hcHBpbmctYWxmb25zLnppdHRlcmJhY2tlQHZtLm9yYWNsZS5jb20iLCJvcmdhbml6YXRpb25hbF91bml0IjoiQU4iLCJkZXBhcnRtZW50X251bWJlciI6IkFOXzFfMSJ9.SH3CARJgIVVziP4sI9Mavjf89LJkyA-wknR8IithhGRDY4l6fVtYCemKACU1ctHPQkHRax87BJ_590UYQPtAUTkpDsBfr4XFxE6wXf0StRRqNhP62p1egSZhXouBUr0VeGvpGMTOnsTqRKr52oE-eAkLBB6YkoEKvgQDdeq5BCRpvZo-Jv2JwMgSqhPOMR9fh3VNJHx6S2b62MkNT_Vw630xAOi4jcixvwHo_78Hs1QYXlw4UxzOP4sc2kK6WdXXz_IIFu_pKmJAy9QXPKAxZGmiKzU4X5yfMdhsleTbXPuPOBdn46OYauy9gXVN5NKrzJyPeY9LN_xOTItGlbF8VQ"
, "access_token"  : "eyJ4NXQiOiJlRnpFQmNkX0N3SV9qRlk1bXJlVEd1OGthWlEiLCJraWQiOiJTZWN1cmVEb21haW4yIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhbjQ3MTExMjMiLCJwMjBfdWlkIjoiVC0zNi0xNS0xNS0xMDEtQU40NzExMTIzIiwiaXNzIjoiaHR0cHM6Ly9zc28uY2lubmFtb25zdGFyLm5ldDo0NDMvb2F1dGgyIiwiZ3JvdXBzIjpbIlpJTVBfQURNSU4iXSwic2Vzc2lvbklkIjoiMWQ4MGMzMTAtM2RkMy00ZGMzLThlMjItMDkzZmYwNmMzYjk4fEVsNnpQelpBQkkzSS9ob1BZNzBTYm5xU3hlUVJwM2o3VzlPeUFwenlKd1U9IiwiZ2l2ZW5fbmFtZSI6IkFsZm9ucyIsImZheF9udW1iZXIiOiIrNDkgMTc3IDU5NDggMjIyIiwiZGVwYXJ0bWVudF9udW1iZXIiOiJBTl8xXzEiLCJkaXZpc2lvbiI6IkFOXzEiLCJhdWQiOlsiaHR0cHM6Ly9zc28uY2lubmFtb25zdGFyLm5ldDo0NDMvb2F1dGgyIiwiWklNUCIsInppbXAiXSwiaWRwIjoiQU4iLCJzY29wZSI6WyJvcGVuaWQiLCJaSU1QLk1haW4iXSwiZG9tYWluIjoiU2VjdXJlRG9tYWluMiIsImNsaWVudCI6InppbXAiLCJwaG9uZV9udW1iZXIiOiIrNDkgKDApMTc3IDU5NDggNDM3IiwiZXhwIjoxNzI5NDYzNTU2LCJncmFudCI6IkFVVEhPUklaQVRJT05fQ09ERSIsImlhdCI6MTcyOTQ1NjM1NiwiZmFtaWx5X25hbWUiOiJaaXR0ZXJiYWNrZSIsImp0aSI6Ijh0THVFVTQzTXpHM21ibWw4QTFQREEiLCJlbWFpbCI6IndlZ2VuLW9pZGMtbWFwcGluZy1hbGZvbnMueml0dGVyYmFja2VAdm0ub3JhY2xlLmNvbSIsIm9yZ2FuaXphdGlvbmFsX3VuaXQiOiJBTiJ9.AUVzIWSwmoEFo30gl4z48aj8BX5YcjIaX4kdImwifUYlX-85fGchPXYDnhePDqLvXmN6UsVNIiWcMixx_DIh4Pkla_Yp4sMrTw7xOpMQ0io4Pj8op2jjGvx0h3DDgnWPs2GCduPUTKb6gNplS4hn2JyhpN_Zh5G9wPUplZK7rGzayhzrAkldIlgJAqSg9FlFNEBeKJPO1Z61M_QZ-MQe8cA9H3OnJCGKWSndjyxiE-QCITxYGTui2Xhk2LYxt8SFS9dCjWLppaHbmb22vOhLWD6CoNecZQJhzFajpW-UvFOGf5LNORWjuCMxIGlkwUPANcXMiVq1sUBAWofqfxT54A"
, "refresh_token" : "7qxAVEyJqskxsWow6hVwCA%3D%3D%7ESXjCu58tTiM3mfu6sCyA9151E1Mfw4RdGyK5Bvn62lTl1PK8OogqZa89Skk5%2BiuMDb3e9ViWHxqmL64sC829BHCfBnMr3Ba5%2BYjF7Jy%2FtstNnKZQDZL2lpSv2spKV0mLa1dJrlAOurJydr0DnB0GEQVktLPMnGw%2BtyGUr%2B9Z1KI0Tb10gwgRMXigHMs6dFiU0wHFgjaCQEq3yDwXoAmeIVH6R%2BG2O90YJae0G4wUjUbL6cWijnTDgjYoXm595zLV"
, "scope"         : "openid ZIMP.Main"
, "expires_in"    : 7200
}