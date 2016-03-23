;============================
;BSCVMBSR MODULE
;Generated with TypeWriter][
;============================
Const BSCVMBSR_MAX%=1
Global bscvmBSRId.bscvmBSR[BSCVMBSR_MAX%]
Global bscvmBSRIndex.stack=stackIndexCreate(BSCVMBSR_MAX%)
Global bscvmBSRAvailable.stack=stackIndexCreate(BSCVMBSR_MAX%)

Type bscvmBSR
	Field id%
	Field parentid%
	Field typeid%
	Field name$
	Field instruction$[4095]
	Field instructionPTR%
	Field instructionsize%
	Field stack$[127]
	Field stackPTR%
	Field arg$[15]
	Field call%
	Field ax$
	Field cx$
	Field zero%
	Field run%
	Field schedule% 
	Field duration%
	Field durationCTR%
	Field bscvmVar.bscvmVar[64]
	Field bscvmLabel.bscvmlabel[64]
End Type

Function bscvmBSRStart()
End Function

Function bscvmBSRStop()
	;Purpose:
	;Parameters:
	;Return:
	For this.bscvmBSR=Each bscvmBSR
		bscvmBSRDelete(this)
	Next
End Function

Function bscvmBSRNew.bscvmBSR()
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmBSR=New bscvmBSR
	this\id%=0
	this\parentid%=0
	this\typeid%=0
	this\name$=""
	For loop=1 To 1
		this\instruction$[loop]=""
	Next
	this\instructionPTR%=0
	this\instructionsize%=0
	For loop=1 To 1
		this\stack$[loop]=""
	Next
	this\stackPTR%=0
	For loop=1 To 1
		this\arg$[loop]=""
	Next
	this\call%=0
	this\ax$=""
	this\cx$=""
	this\zero%=0
	this\run%=0
	this\schedule% =0
	this\duration%=0
	this\durationCTR%=0
	For loop=1 To 1
		this\bscvmVar.bscvmVar[loop]=bscvmVarNew()
	Next
	For loop=1 To 1
		this\bscvmLabel.bscvmlabel[loop]=bscvmlabelNew()
	Next
	this\id%=StackPop(bscvmBSRIndex.stack)
	bscvmBSRId[this\id%]=this
	Return this
End Function

Function bscvmBSRDelete(this.bscvmBSR)
	;Purpose:
	;Parameters:
	;Return:
	bscvmBSRId[this\id]=Null
	StackPush(bscvmBSRIndex.stack,this\id%)
	For loop=1 To 1
		bscvmlabelDelete(this\bscvmLabel[loop])
	Next
	For loop=1 To 1
		bscvmVarDelete(this\bscvmVar[loop])
	Next
	this\cx$=""
	this\ax$=""
	For loop=1 To 1
		this\arg$[loop]=""
	Next
	For loop=1 To 1
		this\stack$[loop]=""
	Next
	For loop=1 To 1
		this\instruction$[loop]=""
	Next
	this\name$=""
	Delete this
End Function

Function bscvmBSRUpdate()
	;Purpose:
	;Parameters:
	;Return:
	For this.bscvmBSR=Each bscvmBSR
	Next
End Function

Function bscvmBSRRead.bscvmBSR(file)
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmBSR=New bscvmBSR
	this\id%=ReadInt(file)
	this\parentid%=ReadInt(file)
	this\typeid%=ReadInt(file)
	this\name$=ReadLine(file)
	For loop=1 To 1
		this\instruction$[loop]=ReadLine(file)
	Next
	this\instructionPTR%=ReadInt(file)
	this\instructionsize%=ReadInt(file)
	For loop=1 To 1
		this\stack$[loop]=ReadLine(file)
	Next
	this\stackPTR%=ReadInt(file)
	For loop=1 To 1
		this\arg$[loop]=ReadLine(file)
	Next
	this\call%=ReadInt(file)
	this\ax$=ReadLine(file)
	this\cx$=ReadLine(file)
	this\zero%=ReadInt(file)
	this\run%=ReadInt(file)
	this\schedule% =ReadInt(file)
	this\duration%=ReadInt(file)
	this\durationCTR%=ReadInt(file)
	For loop=1 To 1
		this\bscvmVar.bscvmVar[loop]=bscvmVarRead(file)
	Next
	For loop=1 To 1
		this\bscvmLabel.bscvmlabel[loop]=bscvmlabelRead(file)
	Next
	Return this
End Function

Function bscvmBSRWrite(file,this.bscvmBSR)
	;Purpose:
	;Parameters:
	;Return:
	WriteInt(file,this\id%)
	WriteInt(file,this\parentid%)
	WriteInt(file,this\typeid%)
	WriteLine(file,this\name$)
	For loop=1 To 1
		WriteLine(file,this\instruction$[loop])
	Next
	WriteInt(file,this\instructionPTR%)
	WriteInt(file,this\instructionsize%)
	For loop=1 To 1
		WriteLine(file,this\stack$[loop])
	Next
	WriteInt(file,this\stackPTR%)
	For loop=1 To 1
		WriteLine(file,this\arg$[loop])
	Next
	WriteInt(file,this\call%)
	WriteLine(file,this\ax$)
	WriteLine(file,this\cx$)
	WriteInt(file,this\zero%)
	WriteInt(file,this\run%)
	WriteInt(file,this\schedule% )
	WriteInt(file,this\duration%)
	WriteInt(file,this\durationCTR%)
	For loop=1 To 1
		bscvmVarWrite(file,this\bscvmVar.bscvmVar[loop])
	Next
	For loop=1 To 1
		bscvmlabelWrite(file,this\bscvmLabel.bscvmlabel[loop])
	Next
End Function

Function bscvmBSROpen(filename$="Default")
	;Purpose:
	;Parameters:
	;Return:
	file=ReadFile(filename+".bscvmBSR")
	Repeat
		bscvmBSRRead(file)
	Until Eof(file)
	CloseFile(file)
End Function

Function bscvmBSRSave(filename$="Default")
	;Purpose:
	;Parameters:
	;Return:
	file=WriteFile(filename$+".bscvmBSR")
	For this.bscvmBSR= Each bscvmBSR
		bscvmBSRWrite(file,this)
	Next
	CloseFile(file)
End Function

Function bscvmBSRCopy.bscvmBSR(this.bscvmBSR)
	;Purpose:
	;Parameters:
	;Return:
	copy.bscvmBSR=New bscvmBSR
	copy\id%=this\id%
	copy\parentid%=this\parentid%
	copy\typeid%=this\typeid%
	copy\name$=this\name$
	For loop=1 To 1
		copy\instruction$[loop]=this\instruction$[loop]
	Next
	copy\instructionPTR%=this\instructionPTR%
	copy\instructionsize%=this\instructionsize%
	For loop=1 To 1
		copy\stack$[loop]=this\stack$[loop]
	Next
	copy\stackPTR%=this\stackPTR%
	For loop=1 To 1
		copy\arg$[loop]=this\arg$[loop]
	Next
	copy\call%=this\call%
	copy\ax$=this\ax$
	copy\cx$=this\cx$
	copy\zero%=this\zero%
	copy\run%=this\run%
	copy\schedule% =this\schedule% 
	copy\duration%=this\duration%
	copy\durationCTR%=this\durationCTR%
	For loop=1 To 1
		copy\bscvmVar.bscvmVar[loop]=bscvmVarCopy(this\bscvmVar[loop])
	Next
	For loop=1 To 1
		copy\bscvmLabel.bscvmlabel[loop]=bscvmlabelCopy(this\bscvmLabel[loop])
	Next
	copy\id%=StackPop(bscvmBSRIndex.stack)
	bscvmBSRId[copy\id%]=copy
	Return copy
End Function

Function bscvmBSRMimic(mimic.bscvmBSR,this.bscvmBSR)
	;Purpose:
	;Parameters:
	;Return:
	mimic\id%=this\id%
	mimic\parentid%=this\parentid%
	mimic\typeid%=this\typeid%
	mimic\name$=this\name$
	For loop=1 To 1
		mimic\instruction$[loop]=this\instruction$[loop]
	Next
	mimic\instructionPTR%=this\instructionPTR%
	mimic\instructionsize%=this\instructionsize%
	For loop=1 To 1
		mimic\stack$[loop]=this\stack$[loop]
	Next
	mimic\stackPTR%=this\stackPTR%
	For loop=1 To 1
		mimic\arg$[loop]=this\arg$[loop]
	Next
	mimic\call%=this\call%
	mimic\ax$=this\ax$
	mimic\cx$=this\cx$
	mimic\zero%=this\zero%
	mimic\run%=this\run%
	mimic\schedule% =this\schedule% 
	mimic\duration%=this\duration%
	mimic\durationCTR%=this\durationCTR%
	For loop=1 To 1
		bscvmVarMimic(mimic\bscvmVar[loop],this\bscvmVar[loop])
	Next
	For loop=1 To 1
		bscvmlabelMimic(mimic\bscvmLabel[loop],this\bscvmLabel[loop])
	Next
End Function

Function bscvmBSRCreate.bscvmBSR(id%,parentid%,typeid%,name$,instruction1$,instructionPTR%,instructionsize%,stack1$,stackPTR%,arg1$,call%,ax$,cx$,zero%,run%,schedule% ,duration%,durationCTR%,bscvmVar1.bscvmVar,bscvmLabel1.bscvmlabel)
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmBSR=bscvmBSRNew()
	this\id%=id%
	this\parentid%=parentid%
	this\typeid%=typeid%
	this\name$=name$
	this\instruction$[1]=instruction1$
	this\instructionPTR%=instructionPTR%
	this\instructionsize%=instructionsize%
	this\stack$[1]=stack1$
	this\stackPTR%=stackPTR%
	this\arg$[1]=arg1$
	this\call%=call%
	this\ax$=ax$
	this\cx$=cx$
	this\zero%=zero%
	this\run%=run%
	this\schedule% =schedule% 
	this\duration%=duration%
	this\durationCTR%=durationCTR%
	this\bscvmVar.bscvmVar[1]=bscvmVar1.bscvmVar
	this\bscvmLabel.bscvmlabel[1]=bscvmLabel1.bscvmlabel
	Return this
End Function

Function bscvmBSRSet(this.bscvmBSR,id%,parentid%,typeid%,name$,instruction1$,instructionPTR%,instructionsize%,stack1$,stackPTR%,arg1$,call%,ax$,cx$,zero%,run%,schedule% ,duration%,durationCTR%,bscvmVar1.bscvmVar,bscvmLabel1.bscvmlabel)
	;Purpose:
	;Parameters:
	;Return:
	this\id%=id%
	this\parentid%=parentid%
	this\typeid%=typeid%
	this\name$=name$
	this\instruction$[1]=instruction1$
	this\instructionPTR%=instructionPTR%
	this\instructionsize%=instructionsize%
	this\stack$[1]=stack1$
	this\stackPTR%=stackPTR%
	this\arg$[1]=arg1$
	this\call%=call%
	this\ax$=ax$
	this\cx$=cx$
	this\zero%=zero%
	this\run%=run%
	this\schedule% =schedule% 
	this\duration%=duration%
	this\durationCTR%=durationCTR%
	this\bscvmVar.bscvmVar[1]=bscvmVar1.bscvmVar
	this\bscvmLabel.bscvmlabel[1]=bscvmLabel1.bscvmlabel
End Function