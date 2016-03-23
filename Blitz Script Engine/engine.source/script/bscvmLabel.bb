;============================
;BSCVMLabel MODULE
;Generated with TypeWriter][
;============================
Const BSCVMLabel_MAX%=512
Dim bscvmLabelId.bscvmLabel(BSCVMLabel_MAX%)
Global bscvmLabelIndex.stack=stackIndexCreate(BSCVMLabel_MAX%)

Type bscvmLabel
	Field id%
	Field offset%
End Type

Function bscvmLabelStart()
End Function

Function bscvmLabelStop()
	For this.bscvmLabel=Each bscvmLabel
		bscvmLabelDelete(this)
	Next
End Function

Function bscvmLabelNew.bscvmLabel()
	this.bscvmLabel=New bscvmLabel
	this\id%=StackPop(bscvmLabelIndex.stack)
	bscvmLabelId(this\id%)=this	
	Return this
End Function

Function bscvmLabelDelete(this.bscvmLabel)
	bscvmLabelId(this\id)=Null
	StackPush(bscvmLabelIndex.stack,this\id%)
	Delete this
End Function

Function bscvmLabelUpdate()
	For this.bscvmLabel=Each bscvmLabel
	Next
End Function

Function bscvmLabelRead.bscvmLabel(file)
	this.bscvmLabel=New bscvmLabel
	this\id%=ReadInt(file)
	Return this
End Function

Function bscvmLabelWrite(file,this.bscvmLabel)
	WriteInt(file,this\id%)
End Function

Function bscvmLabelOpen(filename$="Default")
	file=ReadFile(filename+".bscvmLabel")
	Repeat
		bscvmLabelRead(file)
	Until Eof(file)
	CloseFile(file)
End Function

Function bscvmLabelSave(filename$="Default")
	file=WriteFile(filename$+".bscvmLabel")
	For this.bscvmLabel= Each bscvmLabel
		bscvmLabelWrite(file,this)
	Next
	CloseFile(file)
End Function

Function bscvmLabelCopy.bscvmLabel(this.bscvmLabel)
	copy.bscvmLabel=New bscvmLabel
	copy\id%=this\id%
	Return copy
End Function

Function bscvmLabelMimic(mimic.bscvmLabel,this.bscvmLabel)
	mimic\id%=this\id%
End Function

Function bscvmLabelCreate.bscvmLabel(id=0,offset=0)
	For this.bscvmLabel=Each bscvmLabel
		If this\id=id 
			this\offset=offset
			Return this	
		EndIf	
	Next
	this.bscvmLabel=bscvmLabelNew()
	this\offset=offset
	Return this
End Function

Function bscvmLabelSet(this.bscvmLabel,id%)
	this\id%=id%
End Function