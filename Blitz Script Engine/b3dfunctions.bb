

Function execute_function(func$)

If Lower$(func$)="graphics"
 NextToken(codeline$)
 width = dataline\tokenNum
 NextToken(codeline$)
 height = dataline\tokenNum
 NextToken(codeline$)
 depth = dataline\tokenNum
 NextToken(codeline$)
 mode = dataline\tokenNum
 Graphics width,height,depth,mode
EndIf 

End Function
