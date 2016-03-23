Graphics 640,480
SetBuffer BackBuffer()
Type MemoryBlock
	Field this
    Field size
	Field EAP=0 ;pointer
	Field EBP=0 
End Type




mybank.MemoryBlock=CreateMemoryBlock(2550)

MemPokeString(mybank,"hellomoto")
MemPokeByte(mybank,255)
Print MemPeekString$(mybank)
MemPokeInt(mybank,21321254)
MemPokeShort(mybank,45657)
MemPokeInt(mybank,456)
MemSeek(mybank,MemPos(mybank,1))
MemPokeString(mybank,"herld")
Print MemPeekString$(mybank)
MemPokeInt(mybank,656)
MemPokeByte(mybank,255)
MemPokeString(mybank,"hello")

MemPokeString(mybank,"a")
MemPokeString(mybank,"b")
MemPokeString(mybank,"c")
MemPokeString(mybank,"d")
MemPokeString(mybank,"e")
MemPokeString(mybank,"f")
MemPokeString(mybank,"g")
MemPokeString(mybank,"h")
MemPokeString(mybank,"i")
MemPokeString(mybank,"j")
MemPokeString(mybank,"k")
MemPokeString(mybank,"m")
MemPokeString(mybank,"n")
MemPokeString(mybank,"o")
MemPokeString(mybank,"p")
MemPokeString(mybank,"q")
MemPokeString(mybank,"r")
MemPokeString(mybank,"s")
MemPokeString(mybank,"t")
MemPokeString(mybank,"u")
MemPokeString(mybank,"v")
MemPokeString(mybank,"w")
MemPokeString(mybank,"x")
MemPokeString(mybank,"y")
MemPokeString(mybank,"z")
For i = 1 To 150
MemPokeByte(mybank,i)
Next

MemSeek(mybank,0)
Print MemPeekString$(mybank)
Print MemPeekInt(mybank)
Print MemPeekShort(mybank)
Print MemPeekInt(mybank)
Print MemPeekString$(mybank)
Print MemPeekInt(mybank)
Print MemPeekByte(mybank)
Print MemPeekString$(mybank)
Cls
f=0
t=16
DumpMemoryBlock(mybank,f,t)
Flip
Repeat
	If KeyDown(208)
	f=f+1
	t=t+1
	DumpMemoryBlock(mybank,f,t)
	Flip
	EndIf
	If KeyDown(200)
	f=f-1
	t=t-1
	DumpMemoryBlock(mybank,f,t)
	Flip
	EndIf
Until KeyHit(1)


Function CreateMemoryBlock.MemoryBlock(size)
	myMemory.MemoryBlock = New MemoryBlock
	myMemory\size = size
	myMemory\this = CreateBank(size)
	Return myMemory
End Function

Function DumpMemoryBlock(mem.MemoryBlock,f=0,t=0)
	MemSeek(mem,0)
	For i = f To t
		hx$=""
		a$=""
		Stop
		For j = 0 To 15
		    tmp=PeekByte(mem\this,i*15+j)
			If tmp = 0 
				a$=a$+"."
			Else
			a$=a$+Chr$(tmp)
			EndIf
			
			If j = 7 hx$=hx$+"- "		
			hx$=hx$+Right$(Hex$(tmp),2)+" "
			
		Next
		Text 0,i*15+i, Left$(Hex$(i*16),4)+":"+Right$(Hex$(i*16),4)+" "+hx$+a$
		
	Next

End Function 

Function FreeMemoryBlock(mem.MemoryBlock)
	FreeBank mem\this
	Delete mem
End Function 

Function MemPokeByte(mem.MemoryBlock,value)
	PokeByte(mem\this,mem\EAP,value)
	mem\EAP=mem\EAP+1
End Function 

Function MemPeekByte(mem.MemoryBlock)
	value=PeekByte(mem\this,mem\EBP)
	mem\EBP=mem\EBP+1
	Return value 
End Function 

Function MemPokeFloat(mem.MemoryBlock,value)
	PokeFloat(mem\this,mem\EAP,value)
	mem\EAP=mem\EAP+4
End Function 

Function MemPeekFloat(mem.MemoryBlock)
	value=PeekFloat(mem\this,mem\EBP)
	mem\EBP=mem\EBP+4
	Return value 
End Function 

Function MemPokeInt(mem.MemoryBlock,value)
	PokeInt(mem\this,mem\EAP,value)
	mem\EAP=mem\EAP+4
End Function 

Function MemPeekInt(mem.MemoryBlock)
	value=PeekInt(mem\this,mem\EBP)
	mem\EBP=mem\EBP+4
	Return value 
End Function 

Function MemPokeShort(mem.MemoryBlock,value)
	PokeShort(mem\this,mem\EAP,value)
	mem\EAP=mem\EAP+2
End Function 

Function MemPeekShort(mem.MemoryBlock)
	value=PeekShort(mem\this,mem\EBP)
	mem\EBP=mem\EBP+2
	Return value 
End Function 

Function MemPokeString(mem.MemoryBlock,s$)

	For i = 1 To Len(s$)
		PokeByte(mem\this,mem\EAP,Asc(Mid$(s$,i,1)))
		mem\EAP=mem\EAP+1
	Next
		PokeByte(mem\this,mem\EAP,0)
		mem\EAP=mem\EAP+1
	
End Function

Function MemPeekString$(mem.MemoryBlock)
    a$=""
	If mem\EBP <> 0:pos=mem\EBP:Else:pos=0:EndIf
	Repeat	
		tmp=PeekByte(mem\this,pos)
		If tmp = 0
			mem\EBP=pos+1
			Return a$
		EndIf
		pos=pos+1
		a$=a$+Chr$(tmp)
	Forever 	
		
End Function 


;MemPos(memoryType,WritePointerFlag?)
;Returns value either ReadPointer (default) or WritePointer
Function MemPos(mem.MemoryBlock,flag=0)
	If flag=1
		Return mem\EAP
	Else
		Return mem\EBP
	EndIf
End Function 

Function MemSeek(mem.MemoryBlock,pos)
	mem\EAP=pos
	mem\EBP=pos
End Function