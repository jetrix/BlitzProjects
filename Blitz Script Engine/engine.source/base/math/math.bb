;Combine 3 integer values into 1 large value
;and have them extracted later.
;Max value appx a=2140 b=9999 c=99
Function CombABC(a=0,b=0,c=0)
	Return (a*1000000)+(b*100)+c
End Function 

Function GetA(n)
	Return Int(n/1000000)
End Function 

Function GetB(n)
	Return Int((n-GetA(n)*1000000)/100)
End Function

Function GetC(n)
	Return n-(GetA(n)*1000000)-(GetB(n)*100)
End Function


