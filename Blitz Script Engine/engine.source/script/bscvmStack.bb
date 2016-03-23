;============================
;STACK MODULE
;Generated with TypeWriter][
;============================
Const STACK_MAX%=0

Type stack
	Field pointer%
	Field size%
	Field bank%
End Type

Function stackStart()
End Function

Function stackStop()
	For this.stack=Each stack
		stackDelete(this)
	Next
End Function

Function stackNew.stack()
	this.stack=New stack
	this\pointer%=0
	this\size%=0
	this\bank%=0
	Return this
End Function

Function stackDelete(this.stack)
	Freebank this\bank%
	Delete this
End Function

Function stackUpdate()
	For this.stack=Each stack
	Next
End Function

Function stackRead.stack(file)
	this.stack=New stack
	this\pointer%=ReadInt(file)
	this\size%=ReadInt(file)
	this\bank%=ReadInt(file)
	Return this
End Function

Function stackWrite(file,this.stack)
	WriteInt(file,this\pointer%)
	WriteInt(file,this\size%)
	WriteInt(file,this\bank%)
End Function

Function stackOpen(filename$="Default")
	file=ReadFile(filename+".stack")
	Repeat
		stackRead(file)
	Until Eof(file)
	CloseFile(file)
End Function

Function stackSave(filename$="Default")
	file=WriteFile(filename$+".stack")
	For this.stack= Each stack
		stackWrite(file,this)
	Next
	CloseFile(file)
End Function

;Function stackCSVRead(level.level)
;	this.stack=stackNew()
;	this\pointer%=level\csvfield$[2]
;	this\size%=level\csvfield$[3]
;	this\bank%=level\csvfield$[4]
;End Function

;Function stackCSVHeaderWrite(file,this.stack)
;	WriteLine(file,"stacks,"+"pointer%"+","+"size%"+","+"bank%")
;End Function

;Function stackCSVWrite(file,this.stack)
;	WriteLine(file,"stack,"+Str(this\pointer%)+","+Str(this\size%)+","+Str(this\bank%))
;End Function

;Function stackCSVSave(file%)
;	stackCSVHeaderWrite(file%,this.stack)
;	For this.stack= Each stack
;		stackCSVWrite(file%,this)
;	Next
;End Function

Function stackCopy.stack(this.stack)
	copy.stack=New stack
	copy\pointer%=this\pointer%
	copy\size%=this\size%
	copy\bank%=this\bank%
	Return copy
End Function

Function stackMimic(mimic.stack,this.stack)
	mimic\pointer%=this\pointer%
	mimic\size%=this\size%
	mimic\bank%=this\bank%
End Function

Function stackCreate.stack(size%)
	this.stack=stacknew()
	this\size%=size%
	this\bank%=CreateBank(4*this\size%)
	Return this
End Function

Function stackSet(this.stack,pointer%,size%,bank%)
	this\pointer%=pointer%
	this\size%=size%
	this\bank%=bank%
End Function

Function stackIndexCreate.stack(size%)
	this.stack=stackCreate(size%)
	For loop = size% To 1 Step -1
		stackPush(this,loop)
	Next		
	Return this
End Function

Function stackIndexReset(this.stack)
	this\pointer=0
	For loop = this\size% To 1 Step -1
		stackPush(this,loop)
	Next		
End Function


Function stackPush%(this.stack,value%)
	PokeInt(this\bank%,this\pointer%,value%)
	this\pointer%=this\pointer%+4
End Function

Function stackPop%(this.stack)
	this\pointer%=this\pointer%-4
	Return PeekInt(this\bank%,this\pointer%)
End Function