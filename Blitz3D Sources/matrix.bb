; declare initial variables

Global x,y,s$,colpos, symbol$,clear = False, reset = True,stran = 0
Const ascii$ = "ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"


Dim strand$(44,34) ;the string value of a block
Dim strandcol(44) ; column position of block
Dim strandpos(44) ; row position of column
Dim strandColor(44)


;set graphics and initialize 
Graphics 640,480,16,1
;SetBuffer BackBuffer()
SeedRnd MilliSecs() 


;load neccesary objects
matrixfont = LoadFont("Arial",14)
SetFont matrixfont
Reset()




;main loop
Repeat

	DrawSymbol()

	If reset = True 
		ResetStrand(stran)
		reset = False
		clear = True
	EndIf

	If clear = True 				 
		clearStrand(stran)
		;Delay 10				
	EndIf
	If clear = False 
		stran = stran + 5
	EndIf
	
	If KeyHit(2) Then Reset()
;Flip
Until KeyHit(1)





Function DrawSymbol()
	curr = rndCols()
	strandcol(curr)=curr*14
	strandBlock(curr)
	Color 0,strandColor(curr),0
	Text strandcol(curr),strandpos(curr),rndSymbol$()
End Function

Function SetColor()
	Color 0,Rnd(255)+240,0
End Function

Function Reset()
For ctr = 0 To 44
	For ctr2 = 0 To 34
	strand$ (ctr,ctr2)=""
	Next
	strandpos(ctr)=0
	strandColor(ctr) = Rnd(255)+250
Next
End Function

Function rndCols()
	colpos = Rnd(44)
	Return colpos
End Function

Function rndSymbol$()
	symbol$ = Mid$(ascii$,Rnd(61)+1,1)
	Return symbol$
End Function

Function strandBlock(column)
	strandpos(column) = strandpos(column) + 14
End Function


Function ResetStrand(num)
	strandpos(num)=0
	strandColor(num)=Rnd(255)+250
End Function

Function ClearStrand(d) 

	
	Color 0,0,0	
	strandBlock(d)
		Rect strandcol(d)-3,strandpos(d)+2,12,12,1
		Delay 5
	 If strandpos(d) > 480 
		 ResetStrand(d)
		 clear = False
		 reset = True 
		 
	 EndIf
End Function