;============================
;bscvmVar MODULE
;Generated with TypeWriter][
;============================
Const BSCVMVAR_MAX%=128
Const BLITZSCRIPTVAR_MAX%=32
Dim bscvmVarId.bscvmVar(BSCVMVAR_MAX%)
Global bscvmVarIndex.stack=stackIndexCreate(BSCVMVAR_MAX%)
Global blitzscriptVar$[BLITZSCRIPTVAR_MAX%]

Type bscvmVar
	Field id%
	Field value$
End Type

Function bscvmVarStart()
End Function

Function bscvmVarStop()
	For this.bscvmVar=Each bscvmVar
		bscvmVarDelete(this)
	Next
End Function

Function bscvmVarNew.bscvmVar()
	this.bscvmVar=New bscvmVar
	this\id%=0
	this\value$=""
	this\id%=StackPop(bscvmVarIndex.stack)
	bscvmVarId(this\id%)=this
	Return this
End Function

Function bscvmVarDelete(this.bscvmVar)
	bscvmVarId(this\id)=Null
	StackPush(bscvmVarIndex.stack,this\id%)
	this\value$=""
	Delete this
End Function

Function bscvmVarUpdate()
	For this.bscvmVar=Each bscvmVar
	Next
End Function

Function bscvmVarRead.bscvmVar(file)
	this.bscvmVar=New bscvmVar
	this\id%=ReadInt(file)
	this\value$=ReadLine(file)
	Return this
End Function

Function bscvmVarWrite(file,this.bscvmVar)
	WriteInt(file,this\id%)
	WriteLine(file,this\value$)
End Function

Function bscvmVarOpen(filevalue$="Default")
	file=ReadFile(filevalue+".bscvmVar")
	Repeat
		bscvmVarRead(file)
	Until Eof(file)
	CloseFile(file)
End Function

Function bscvmVarSave(filevalue$="Default")
	file=WriteFile(filevalue$+".bscvmVar")
	For this.bscvmVar= Each bscvmVar
		bscvmVarWrite(file,this)
	Next
	CloseFile(file)
End Function

Function bscvmVarCopy.bscvmVar(this.bscvmVar)
	copy.bscvmVar=New bscvmVar
	copy\id%=this\id%
	copy\value$=this\value$
	copy\id%=StackPop(bscvmVarIndex.stack)
	bscvmVarId(copy\id%)=copy
	Return copy
End Function

Function bscvmVarMimic(mimic.bscvmVar,this.bscvmVar)
	mimic\id%=this\id%
	mimic\value$=this\value$
End Function

Function bscvmVarCreate.bscvmVar(value$="",id=0)
	For this.bscvmVar=Each bscvmVar
		If id>0 And this\id=id Return this
		If id=0 And Lower(this\value$)=Lower(value$) Return this
	Next
	this.bscvmVar=bscvmVarNew()
	this\value$=value$
	Return this
End Function

Function bscvmVarSet(this.bscvmVar,id%,value$)
	this\id%=id%
	this\value$=value$
End Function

Function blitzscriptVarSet(blitzscriptvarid,value$)
	blitzscriptVar[blitzscriptvarid]=value	
End Function

Function blitzscriptVarGet$(blitzscriptvarid)
	Return blitzscriptVar[blitzscriptvarid]
End Function