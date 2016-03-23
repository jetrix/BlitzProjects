;============================
;BSCVMTHREAD MODULE
;Generated with TypeWriter][
;============================
;BlitzScript3D  Virtual Machine (B.S.C.V.M) by Frankie Taylor

Const BSCVMTHREAD_MAX%=256
Const BSCVMTHREAD_ARG_MAX%=15
Const BSCVMTHREAD_ARGSTACK_MAX%=127
Const BSCVMTHREAD_INSTRUCTION_MAX%=4097
Const BSCVMTHREAD_STACK_MAX%=127

Const	BSCVM_OP_MOV_EAX_ECX	=	1	;$01	
Const 	BSCVM_OP_MOV_ECX_EAX	=	2	;$02	
Const	BSCVM_OP_MOV_EAX_VAR	=	3	;$03	
Const	BSCVM_OP_MOV_ECX_VAR	=	4	;$04	
Const	BSCVM_OP_MOV_VAR_EAX	=	5	;$05	
Const	BSCVM_OP_MOV_VAR_ECX	=	6	;$06	
Const	BSCVM_OP_MOV_EAX_VALUE	=	7	;$07	
Const	BSCVM_OP_MOV_ECX_VALUE	=	8	;$08	
Const	BSCVM_OP_MOV_VALUE_EAX	=	9	;$09	
Const	BSCVM_OP_MOV_VALUE_ECX	=	10	;$0A	
Const	BSCVM_OP_PUSH_EAX	=	11	;$0B	
Const	BSCVM_OP_POP_EAX	=	12	;$0C	
Const	BSCVM_OP_POP_ECX	=	13	;$0D	
Const	BSCVM_OP_POW_EAX_ECX	=	14	;$0E	
Const	BSCVM_OP_MUL_EAX_ECX	=	15	;$0F	
Const	BSCVM_OP_SHL_EAX_ECX	=	16	;$10	
Const	BSCVM_OP_DIV_EAX_ECX	=	17	;$11	
Const	BSCVM_OP_SHR_EAX_ECX	=	18	;$12	
Const	BSCVM_OP_ADD_EAX_ECX	=	19	;$13	
Const	BSCVM_OP_NEG_EAX	=	20	;$14	
Const	BSCVM_OP_SUB_EAX_ECX	=	21	;$15	
Const	BSCVM_OP_INC_EAX	=	22	;$16	
Const	BSCVM_OP_DEC_EAX	=	23	;$17	
Const	BSCVM_OP_SEQ_EAX	=	24	;$18	
Const	BSCVM_OP_SNE_EAX	=	25	;$19	
Const	BSCVM_OP_SLE_EAX	=	26	;$1A	
Const	BSCVM_OP_SGE_EAX	=	27	;$1B	
Const	BSCVM_OP_SGT_EAX	=	28	;$1C	
Const	BSCVM_OP_SLT_EAX	=	29	;$1D	
Const	BSCVM_OP_SLE_STR	=	30	;$1E	
Const	BSCVM_OP_SGE_STR	=	31	;$1F	
Const	BSCVM_OP_SGT_STR	=	32	;$20	
Const	BSCVM_OP_SLT_STR	=	33	;$21	
Const	BSCVM_OP_AND_EAX	=	34	;$22	
Const	BSCVM_OP_OR_EAX	=	35	;$23	
Const	BSCVM_OP_XOR_EAX	=	36	;$24	
Const	BSCVM_OP_JMP	=	37	;$25	
Const	BSCVM_OP_JZ	=	38	;$26	
Const	BSCVM_OP_LABEL	 =	39	;$27	
Const	BSCVM_OP_BSR	=	40	;$28	
Const	BSCVM_OP_PUSH_CALL	=	41	;$29	
Const	BSCVM_OP_POP_CALL	=	42	;2A	
Const	BSCVM_OP_PUSH_ARG	=	43	;2B	
Const	BSCVM_OP_POP_ARG	=	44	;2C	
Const	BSCVM_OP_RETURN	=	45	;2D	
Const	BSCVM_OP_NEWVAR	=	46	;2E	
Const	BSCVM_OP_NEWLABEL	=	47	;2F	
Const	BSCVM_OP_MUL_STR	=	48	;30

Dim bscvmThreadId.bscvmThread(BSCVMTHREAD_MAX%)
Global bscvmThreadIndex.stack=stackIndexCreate(BSCVMTHREAD_MAX%)

Type bscvmThread
	;Purpose:
	Field id%
	Field parentid%
	Field typeid%
	Field name$
	Field instruction$[BSCVMTHREAD_INSTRUCTION_MAX%]
	Field instructionPTR%
	Field instructionsize%
	Field stack$[BSCVMTHREAD_STACK_MAX%]
	Field stackPTR%
	Field arg$[BSCVMTHREAD_ARG_MAX%]
	Field call%
	Field ax$
	Field cx$
	Field zero%
	Field run% ;state -1:delete 0:suspend 1:execute
	Field schedule% 
	Field duration% ;-1:Infinite loop,  0: Load & Delete, 1-n:Specified Loops  
	Field durationCTR%
End Type

Function bscvmThreadStart()
	;Purpose: Exec main.bso script located in game root
	;Parameters: None
	;Return: ScriptID
End Function

Function bscvmThreadStop()
	;Purpose: Removes all bscvmThreads 
	;Parameters: None
	;Return: None
	For this.bscvmThread=Each bscvmThread
		bscvmThreadDelete(this)
	Next
End Function

Function bscvmThreadNew.bscvmThread()
	;Purpose: Creates a new bscvmThread instance
	;Parameters: None
	;Return: bscvmThread Object
	this.bscvmThread=New bscvmThread
	this\id%=0
	this\parentid%=0
	this\typeid%=0
	this\name$=""
	this\instructionPTR%=0
	this\stackPTR%=0
	For loop=1 To 10
		this\arg[loop]=""
	Next
	this\ax=""
	this\cx=""
	this\zero%=0
	this\run%=0
	this\duration%=0
	this\durationCTR%=0
	this\schedule%=0
	this\id%=StackPop(bscvmThreadIndex.stack)
	bscvmThreadId(this\id%)=this
	Return this
End Function

Function bscvmThreadDelete(this.bscvmThread)
	;Purpose: Deletes a bscvmThread Instance
	;Parameters: bscvmThread Object
	;Return: None
	bscvmThreadId(this\id)=Null
	StackPush(bscvmThreadIndex.stack,this\id%)
	For loop=1 To 10
		this\arg[loop]=0.0
	Next
	this\name$=""
	Delete this
End Function

Function bscvmThreadUpdate()
	;Purpose: 
	;Parameters:
	;Return:
	For this.bscvmThread=Each bscvmThread
		Select this\run
			Case 1
				Repeat
					bscvmThreadOp(this)
				Until this\instructionPTR>this\instructionSize
				this\instructionPTR=0 ;reset instruction
				If this\duration>0 
					If this\durationCTR=this\duration
						this\run=False 
					Else
						this\durationCTR=this\durationCTR+1
					EndIf
				EndIf
		End Select	
	Next
End Function

Function bscvmThreadRead.bscvmThread(file)
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmThread=New bscvmThread
	this\id%=ReadInt(file)
	this\parentid%=ReadInt(file)
	this\typeid%=ReadInt(file)
	this\name$=ReadLine(file)
	this\instructionPTR%=ReadInt(file)
	this\stackPTR%=ReadInt(file)
	For loop=1 To 10
		this\arg[loop]=ReadFloat(file)
	Next
	this\ax=ReadFloat(file)
	this\cx=ReadFloat(file)
	this\zero%=ReadInt(file)
	this\run%=ReadInt(file)
	this\schedule%=ReadInt(file)
	this\duration%=ReadInt(file)
	this\durationCTR%=ReadInt(file)
	Return this
End Function

Function bscvmThreadWrite(file,this.bscvmThread)
	;Purpose:
	;Parameters:
	;Return:
	WriteInt(file,this\id%)
	WriteInt(file,this\parentid%)
	WriteInt(file,this\typeid%)
	WriteLine(file,this\name$)
	WriteInt(file,this\instructionPTR%)
	WriteInt(file,this\stackPTR%)
	For loop=1 To 10
		WriteFloat(file,this\arg[loop])
	Next
	WriteFloat(file,this\ax)
	WriteFloat(file,this\cx)
	WriteInt(file,this\zero%)
	WriteInt(file,this\run%)
	WriteInt(file,this\schedule%)
	WriteInt(file,this\duration%)
	WriteInt(file,this\durationCTR%)
End Function

Function bscvmThreadOpen(filename$="Default")
	;Purpose:
	;Parameters:
	;Return:
	file=ReadFile(filename+".bscvmThread")
	Repeat
		bscvmThreadRead(file)
	Until Eof(file)
	CloseFile(file)
End Function

Function bscvmThreadSave(filename$="Default")
	;Purpose:
	;Parameters:
	;Return:
	file=WriteFile(filename$+".bscvmThread")
	For this.bscvmThread= Each bscvmThread
		bscvmThreadWrite(file,this)
	Next
	CloseFile(file)
End Function

Function bscvmThreadCopy.bscvmThread(this.bscvmThread)
	;Purpose:
	;Parameters:
	;Return:
	copy.bscvmThread=New bscvmThread
	copy\id%=this\id%
	copy\parentid%=this\parentid%
	copy\typeid%=this\typeid%
	copy\name$=this\name$
	copy\instructionPTR%=this\instructionPTR%
	copy\stackPTR%=this\stackPTR%
	For loop=1 To 10
		copy\arg[loop]=this\arg[loop]
	Next
	copy\ax=this\ax
	copy\cx=this\cx
	copy\zero%=this\zero%
	copy\run%=this\run%
	copy\schedule%=this\schedule%
	copy\duration%=this\duration%
	copy\durationCTR%=this\durationCTR%
	copy\id%=StackPop(bscvmThreadIndex.stack)
	bscvmThreadId(copy\id%)=copy
	Return copy
End Function

Function bscvmThreadMimic(mimic.bscvmThread,this.bscvmThread)
	;Purpose:
	;Parameters:
	;Return:
	mimic\id%=this\id%
	mimic\parentid%=this\parentid%
	mimic\typeid%=this\typeid%
	mimic\name$=this\name$
	mimic\instructionPTR%=this\instructionPTR%
	mimic\stackPTR%=this\stackPTR%
	For loop=1 To 10
		mimic\arg[loop]=this\arg[loop]
	Next
	mimic\ax=this\ax
	mimic\cx=this\cx
	mimic\zero%=this\zero%
	mimic\run%=this\run%
	mimic\schedule%=this\schedule%
	mimic\duration%=this\duration%
	mimic\durationCTR%=this\durationCTR%
End Function

Function bscvmThreadCreate.bscvmThread(id%,parentid%,typeid%,name$,instruction%,instructionPTR%,stack%,stackPTR%,arg1#,arg2#,arg3#,arg4#,arg5#,arg6#,arg7#,arg8#,arg9#,arg10#,argstack%,argstackPTR%,ax,cx,bx,dx ,zero%,run%,duration%,durationCTR%)
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmThread=bscvmThreadNew()
	this\id%=id%
	this\parentid%=parentid%
	this\typeid%=typeid%
	this\name$=name$
	this\instructionPTR%=instructionPTR%
	this\stackPTR%=stackPTR%
	this\arg[1]=arg1#
	this\arg[2]=arg2#
	this\arg[3]=arg3#
	this\arg[4]=arg4#
	this\arg[5]=arg5#
	this\arg[6]=arg6#
	this\arg[7]=arg7#
	this\arg[8]=arg8#
	this\arg[9]=arg9#
	this\arg[10]=arg10#
	this\ax=ax
	this\cx=cx
	this\zero%=zero%
	this\run%=run%
	this\schedule%=schedule%
	this\duration%=duration%
	this\durationCTR%=durationCTR%
	Return this
End Function

Function bscvmThreadSet(this.bscvmThread,id%,parentid%,typeid%,name$,instruction%,instructionPTR%,stack%,stackPTR%,arg1#,arg2#,arg3#,arg4#,arg5#,arg6#,arg7#,arg8#,arg9#,arg10#,argstack%,argstackPTR%,ax,cx,bx,dx ,zero%,run%,duration%,durationCTR%)
	;Purpose:
	;Parameters:
	;Return:
	this\id%=id%
	this\parentid%=parentid%
	this\typeid%=typeid%
	this\name$=name$
	this\instructionPTR%=instructionPTR%
	this\stackPTR%=stackPTR%
	this\arg[1]=arg1#
	this\arg[2]=arg2#
	this\arg[3]=arg3#
	this\arg[4]=arg4#
	this\arg[5]=arg5#
	this\arg[6]=arg6#
	this\arg[7]=arg7#
	this\arg[8]=arg8#
	this\arg[9]=arg9#
	this\arg[10]=arg10#
	this\ax=ax
	this\cx=cx
	this\zero%=zero%
	this\run%=run%
	this\duration%=duration%
	this\durationCTR%=durationCTR%
End Function

Function bscvmThreadLoad(bscvmInputFilename$)
	;Purpose: BlitzScript3D Command "ScriptLoad" Load  *.bsc Script file. Checks For *.bso version of the file first.
	;	If *.bso found loads it, else compiles *.bsc into *.bso then loads it. 	Note: extension is omitted when called.
	;	Declaration for Variables, Labels, Function Calls are created here.
	;Parameters: *.bss filename
	;Return: bscvmThread Object ID
	file=ReadFile(bscvmInputFilename$+".bso") ;compiled file exists
	If Not file 
		DebugLog bscvmInputFilename+".bsc not compiled";testing
	Else
		DebugLog bscvmInputFilename+".bso found";testing
	EndIf	
	If Not file ;if no compiled file, compile it
		bscvmCompilerLoad(bscvmInputFilename)
		;if noerrors
		file=ReadFile(bscvmInputFilename$+".bso") 
	EndIf
	If file
		DebugLog bscvmInputFilename+".bso compiled and loading"
		this.bscvmThread = bscvmThreadNew()

		While Not Eof(file)
			bscvmDat$=ReadLine(file)
			Select bscvmdat

;				Case BSCVM_OP_MOV_EAX_ECX
;				Case BSCVM_OP_MOV_ECX_EAX
;				Case BSCVM_OP_MOV_EAX_VAR
;				Case BSCVM_OP_MOV_ECX_VAR
;				Case BSCVM_OP_MOV_VAR_EAX
;				Case BSCVM_OP_MOV_VAR_ECX
;				Case BSCVM_OP_MOV_EAX_VALUE
;				Case BSCVM_OP_MOV_ECX_VALUE
;				Case BSCVM_OP_MOV_VALUE_EAX
;				Case BSCVM_OP_MOV_VALUE_ECX
;				Case BSCVM_OP_PUSH_EAX
;				Case BSCVM_OP_POP_EAX
;				Case BSCVM_OP_POP_ECX
;				Case BSCVM_OP_POW_EAX_ECX
;				Case BSCVM_OP_MUL_EAX_ECX
;				Case BSCVM_OP_SHL_EAX_ECX
;				Case BSCVM_OP_DIV_EAX_ECX
;				Case BSCVM_OP_SHR_EAX_ECX
;				Case BSCVM_OP_ADD_EAX_ECX
;				Case BSCVM_OP_NEG_EAX
;				Case BSCVM_OP_SUB_EAX_ECX
;				Case BSCVM_OP_INC_EAX
;				Case BSCVM_OP_DEC_EAX
;				Case BSCVM_OP_SEQ_EAX
;				Case BSCVM_OP_SNE_EAX
;				Case BSCVM_OP_SLE_EAX
;				Case BSCVM_OP_SGE_EAX
;				Case BSCVM_OP_SGT_EAX
;				Case BSCVM_OP_SLT_EAX
;				Case BSCVM_OP_AND_EAX
;				Case BSCVM_OP_OR_EAX
;				Case BSCVM_OP_XOR_EAX
;				Case BSCVM_OP_JMP
;				Case BSCVM_OP_JZ
;				Case BSCVM_OP_LABEL
;				Case BSCVM_OP_BSR
;				Case BSCVM_OP_PUSH_CALL
;				Case BSCVM_OP_POP_CALL
;				Case BSCVM_OP_PUSH_ARG
;				Case BSCVM_OP_POP_ARG
;				Case BSCVM_OP_RETURN
				Case BSCVM_OP_NEWVAR
					id%=ReadLine(file)
					bscvmVar.bscvmVar=bscvmVarCreate(nil$,id%)
					bscvmVar\value=nil
				Case BSCVM_OP_NEWLABEL
					id%=ReadLine(file)
					bscvmLabel.bscvmLabel=bscvmLabelCreate(id%)
					bscvmLabel\offset=ReadLine(file)	
				Default
					this\instruction[this\instructionPTR]=bscvmDat ;ascii format will change
					this\instructionPTR=this\instructionPTR+1
					this\instructionSize=this\instructionPTR-1
	
			End Select
		Wend
		this\instructionPTR=0
		Return this\ID
	EndIf
End Function

Function bscvmThreadExec(bscvmInputFilename$)
	;Purpose: BlitzScript3D Command "ScriptExec" to Load and Run and  *.bsc Script file. 
	;	Checks For *.bso version of the file first.  If *.bso found loads it, 
	;	else compiles *.bsc into *.bso then loads it.
	;Parameters: bscvmInputFilename - *.bsc file to load
	;Return: bscvmThread Object ID
	this.bscvmThread=bscvmThreadID(bscvmThreadLoad(bscvmInputFilename))
	If this<>Null 
		this\run=True
		Return this\id
	EndIf
End Function

Function bscvmThreadRun(ID%, duration%=-1)
	;Purpose: BlitzScript3D Command "ScriptRun" to Starts thread running if stopped.
	;Parameters:
	;	bscvmThread Object ID
	;	duration - loop iterations  -1:Infinite loop,  0: Load & Delete, 1-n:Specified Loop
	;		defaults to -1:Infinite Loop 
	;Return: None
	If bscvmThreadID(ID%)<>Null 
		bscvmThreadID(ID%)\run%=True
		bscvmThreadID(ID%)\duration%=duration%
	EndIf	
End Function	       
	       
Function bscvmThreadHalt(ID%)
	;Purpose: BlitzScript3D Command "ScriptHalt" to Stop running thread
	;Parameters: bscvmThread Object ID
	;Return: None
	If bscvmThreadID(ID%)<>Null 	bscvmThreadID(ID%)\run=False
End Function	       
	       
Function bscvmThreadKill(ID)
	;Purpose: BlitzScript3D Command "ScriptKill" to Delete a thread
	;Parameters: bscvmThread Object ID
	;Return: none
	If bscvmThreadID(ID%)<>Null bscvmThreadDelete(bscvmThreadID(ID%))
End Function	 

Function bscvmThreadRunning(ID)
	;Purpose: BlitzScript3D Command "ScriptRunning" , returns True or False  
	;Parameters: bscvmThread Object ID
	;Return: none
	If bscvmThreadID(ID%)<>Null Return bscvmThreadID(ID%)\run
End Function

Function bscvmThreadSchedule(ID,bscvmSchedule)
	;Purpose: BlitzScript3D Command "ScriptSchedule" to Schedule Script to execute
	;after a certain time
	;Parameters: bscvmThread Object ID
	;Return: none
	If bscvmThreadID(ID%)<>Null
		bscvmThreadID(ID%)\schedule=bscvmSchedule
	EndIf	
End Function

Function bscvmThreadOp(this.bscvmThread)
	;Purpose: Called from the bscVirtualMachineUpdate Function 
	;	Processes bscvmThread Operations, Values, and Stacks
	;Parameters: bscvmThread Object
	;Return: None
	
	Select Int(this\instruction[this\instructionPTR%])
		;x85 Assembly opcodes methods
		Case BSCVM_OP_MOV_ECX_EAX ;assigns this\ax value to this\cx
			this\cx=this\ax
			this\instructionPTR%=this\instructionPTR%+1 ;inc to next instruction
		Case BSCVM_OP_MOV_VAR_EAX ;assigns this\ax value to var
			bscvmVarID(this\instruction[this\instructionPTR%+1])\value=this\ax
			this\instructionPTR%=this\instructionPTR%+2
		Case BSCVM_OP_MOV_EAX_VAR ;assigns var value to this\ax
			this\ax=bscvmVarID(this\instruction[this\instructionPTR%+1])\value
			this\instructionPTR%=this\instructionPTR%+2
		Case BSCVM_OP_MOV_EAX_VALUE ;assigns const value to this\ax
			this\ax=this\instruction[this\instructionPTR%+1]
			this\instructionPTR%=this\instructionPTR%+2
		Case BSCVM_OP_PUSH_EAX ;pushes this\ax value to this\stack
			this\stack[this\stackPTR%]=this\ax
			this\stackPTR%=this\stackPTR%+1
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_POP_ECX ;pops top this\stack value to this\cx
			this\stackPTR%=this\stackPTR%-1
			this\cx=this\stack[this\stackPTR%]
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_POP_EAX ;pops top this\stack value to this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=this\stack[this\stackPTR%]
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_PUSH_CALL ;pushes call value to this\stack
			this\stack[this\stackPTR%]=this\instruction[this\instructionPTR%+1]
			this\stackPTR%=this\stackPTR%+1
			this\instructionPTR%=this\instructionPTR%+2
		Case BSCVM_OP_POP_CALL ;popc ;pops this\stack value to call,executes
			this\stackPTR%=this\stackPTR%-1
			this\call=this\stack[this\stackPTR%];(method)
			bscvmThreadCall(this)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_PUSH_ARG ;pushes this\ax value to argstack
			this\stack[this\stackPTR]=this\ax
			this\stackPTR=this\stackPTR+1
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_POP_ARG ;pops arg value off argstack assign arg
			this\stackPTR=this\stackPTR-1
			this\arg[this\instruction[this\instructionPTR%+1]]=this\stack[this\stackPTR]
			this\instructionPTR%=this\instructionPTR%+2
		Case BSCVM_OP_MUL_EAX_ECX ;integer divide this\ax with this\cx stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
       		this\ax=Float(this\stack[this\stackPTR%])*Float(this\ax)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_MUL_STR
			this\stackPTR%=this\stackPTR%-1
			this\ax=String(this\stack[this\stackPTR%],this\ax)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_POW_EAX_ECX	
			For bscvmloop = 1 To this\cx:this\ax=Float(this\ax)*Float(this\ax):Next ;multiplies this\ax to itself by this\cs times
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_DIV_EAX_ECX ;float divide this\cx with this\ax stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=Float(this\stack[this\stackPTR%])/Float(this\ax)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_NEG_EAX ;
			this\ax=-Float(this\ax)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_ADD_EAX_ECX ;float adds this\ax with this\cx stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=Float(this\stack[this\stackPTR%])+Float(this\ax)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_CONCENATE_EAX_ECX ;float adds this\ax with this\cx stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=this\stack[this\stackPTR%]+this\ax
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SUB_EAX_ECX ;float subtracts this\ax from this\cx stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=Float(this\stack[this\stackPTR%])-Float(this\ax)
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SHL_EAX_ECX ;float shift left this\ax by this\cx stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=this\stack[this\stackPTR%] Shl this\ax
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SHR_EAX_ECX ;float shift right this\ax by this\cx stores value into this\ax
			this\stackPTR%=this\stackPTR%-1
			this\ax=this\stack[this\stackPTR%] Shr this\ax
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_AND_EAX	
			If this\cx And this\ax this\zero=True
			this\instructionPTR%=this\instructionPTR%+1	
		Case BSCVM_OP_OR_EAX	
			If this\cx Or this\ax this\zero=True
			this\instructionPTR%=this\instructionPTR%+1	
		Case BSCVM_OP_XOR_EAX	
			If this\cx Xor this\ax this\zero=True
			this\instructionPTR%=this\instructionPTR%+1	
		Case BSCVM_OP_SLT_EAX ;compares numeric this\ax<this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If Float(this\ax)<Float(this\stack[this\stackPTR%]) this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SGT_EAX ;compares numeric this\ax>this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If Float(this\ax)>Float(this\stack[this\stackPTR%]) this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SLE_EAX ;compares numeric this\ax<=this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If Float(this\ax)<=Float(this\stack[this\stackPTR%]) this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SGE_EAX ;compares numeric this\ax>=this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If Float(this\ax)>=Float(this\stack[this\stackPTR%]) this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SEQ_EAX ;compares numeric/string this\cx<>this\ax sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If this\ax<>this\stack[this\stackPTR%] this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SNE_EAX ;compares numeric/string this\ax=this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If this\ax=this\stack[this\stackPTR%] this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SLT_STR ;compares string this\ax<this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If this\ax<this\stack[this\stackPTR%] this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SGT_STR ;compares string this\ax>this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If this\ax>this\stack[this\stackPTR%] this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SLE_STR ;compares string this\ax<=this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If this\ax<=this\stack[this\stackPTR%] this\zero=True
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_SGE_STR ;compares string this\ax>=this\cx sets script zero flg
			this\stackPTR%=this\stackPTR%-1
			If this\ax>=this\stack[this\stackPTR%] this\zero=True
			this\instructionPTR%=this\instructionPTR%+1	
		Case BSCVM_OP_JZ ;if this\zero true jump to label
  	     	If this\zero
				this\instructionPTR%=bscvmLabelID(this\instruction[this\instructionPTR%+1])\offset
				this\zero=False
			Else
				this\instructionPTR%=this\instructionPTR%+2
			EndIf
		Case BSCVM_OP_JMP ;unconditional jump to label
			this\instructionPTR%=bscvmLabelID(this\instruction[this\instructionPTR%+1])\offset
		Case BSCVM_OP_LABEL
			this\instructionPTR%=this\instructionPTR%+1
		Case BSCVM_OP_BSR
		
	End Select
End Function


Function bscvmThreadCall(this.bscvmThread)
	;Purpose: VirtualMachine Function Call Tree. The Select Case Values correspond to the
	;	bscvmCompilerCallIndex Function Labels.
	;	When adding new functions to be processed by the Virtual Machine ,  
	;	you add the Function Call statement to this select tree.	

	;	Example1 - A function w/ Arguments:	
	;				Case  Function_Index  Your_Function(this\arg[0])

	;	Example2 - A function w/ Return Value:	
	;				Case  Function_Index  this\ax=Your_Function(this\arg[0]) 
	
	;	Example3 - A function with no Arguments
	;				Case  Function_Index  Your_Function()
	
	;	Example4 - A function w/Return Value and no Arguments
	;				Case  Function_Index  this\ax=Your_Function(this\arg[0]) 

	;Parameters: bscvmThread Object
	;Return: None. However, Returned values are stored bcsvmThread\ax 
	this\ax=none
	
	Select this\call

	 	;BLITZ Calls ====================================
	       Case 1 AcceptTCPStream(this\arg[0])
	       Case 2 this\ax=ACos(this\arg[0]);acos macro
	       Case 3 AddAnimSeq(this\arg[0],this\arg[1])
	       Case 4 AddMesh(this\arg[0],this\arg[1])
	       Case 5 AddTriangle(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 6 AddVertex(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6])
	       Case 7 AlignToVector(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 8 AmbientLight(this\arg[0],this\arg[1],this\arg[2])
	       Case 9 Animate(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 10 AnimateMD2(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5])
	       Case 11 Animating(this\arg[0])
	       Case 12 AnimLength(this\arg[0])
	       Case 13 AnimSeq(this\arg[0])
	       Case 14 AnimTime#(this\arg[0])
	       Case 15 AntiAlias(this\arg[0])
	       Case 16 AppTitle(this\arg[0],this\arg[1])
	       Case 17 this\ax=Asc(this\arg[0])
	       Case 18 this\ax=ASin(this\arg[0]);asin macro
	       Case 19 this\ax=ATan(this\arg[0]);atan macro
	       Case 20 this\ax=ATan2(this\arg[0],this\arg[1]);atan2 macro
	       Case 21 AutoMidHandle(this\arg[0])
	       Case 22 this\ax=AvailVidMem();availvidmem macro
	       Case 23 BackBuffer()
	       Case 24 BankSize(this\arg[0])
	       Case 25 Bin$(this\arg[0])
	       Case 26 BrushAlpha(this\arg[0],this\arg[1])
	       Case 27 BrushBlend(this\arg[0],this\arg[1])
	       Case 28 BrushFX(this\arg[0],this\arg[1])
	       Case 29 BrushShininess(this\arg[0],this\arg[1])
	       Case 30 BrushTexture(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 31 BSPAmbientLight(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 32 BSPLighting(this\arg[0],this\arg[1])
	       Case 33 CameraClsColor(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 34 CameraClsMode(this\arg[0],this\arg[1],this\arg[2])
	       Case 35 CameraFogColor(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 36 CameraFogMode(this\arg[0],this\arg[1])
	       Case 37 CameraFogRange(this\arg[0],this\arg[1],this\arg[2])
	       Case 38 CameraPick(this\arg[0],this\arg[1],this\arg[2])
	       Case 39 CameraProject(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 40 CameraRange(this\arg[0],this\arg[1],this\arg[2])
	       Case 41 CameraViewport(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 42 CameraZoom(this\arg[0],this\arg[1])
	       Case 43 CaptureWorld()
	       Case 44 this\ax=Ceil#(this\arg[0]);ceil macro
	       Case 45 ChangeDir(this\arg[0])
	       Case 46 ChannelPan(this\arg[0],this\arg[1])
	       Case 47 ChannelPitch(this\arg[0],this\arg[1])
	       Case 48 ChannelPlaying(this\arg[0])
	       Case 49 ChannelVolume(this\arg[0],this\arg[1])
	       Case 50 Chr(this\arg[0])
	       Case 51 ClearCollisions()
	       Case 52 ClearSurface(this\arg[0],this\arg[1],this\arg[2])
	       Case 53 ClearTextureFilters()
	       Case 54 ClearWorld(this\arg[0],this\arg[1],this\arg[2])
	       Case 55 CloseDir(this\arg[0])
	       Case 56 CloseFile(this\arg[0])
	       Case 57 CloseTCPServer(this\arg[0])
	       Case 58 CloseTCPStream(this\arg[0])
	       Case 59 CloseUDPStream(this\arg[0])
	       Case 60 Cls()
	       Case 61 ClsColor(this\arg[0],this\arg[1],this\arg[2])
	       Case 62 this\ax=CollisionEntity(this\arg[0],this\arg[1]);collisionentity macro
	       Case 63 this\ax=CollisionNX#(this\arg[0],this\arg[1]);collisionnx macro
	       Case 64 this\ax=CollisionNY#(this\arg[0],this\arg[1]);collisionny macro
	       Case 65 this\ax=CollisionNZ#(this\arg[0],this\arg[1]);collisionnz macro
	       Case 66 Collisions(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 67 this\ax=CollisionSurface(this\arg[0],this\arg[1]);collisionsurface macro
	       Case 68 this\ax=CollisionTime(this\arg[0],this\arg[1]);collisiontime macro
	       Case 69 this\ax=CollisionTriangle(this\arg[0],this\arg[1]);collisiontriangle macro
	       Case 70 this\ax=CollisionX#(this\arg[0],this\arg[1]);collisionx macro
	       Case 71 this\ax=CollisionY#(this\arg[0],this\arg[1]);collisiony macro
	       Case 72 this\ax=CollisionZ#(this\arg[0],this\arg[1]);collisionz macro
	       Case 73 Color(this\arg[0],this\arg[1],this\arg[2])
	       Case 74 this\ax=ColorBlue();colorblue macro
	       Case 75 this\ax=ColorGreen();colorgreen macro
	       Case 76 this\ax=ColorRed();colorred macro
	       Case 77 this\ax=CommandLine$()
	       Case 78 CopyBank(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 79 this\ax=CopyEntity(this\arg[0],this\arg[1])
	       Case 80 CopyFile(this\arg[0],this\arg[1])
	       Case 81 this\ax=CopyImage(this\arg[0])
	       Case 82 CopyPixel(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5])
	       Case 83 CopyPixelFast(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5])
	       Case 84 CopyRect(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7])
	       Case 85 CopyStream(this\arg[0],this\arg[1],this\arg[2])
	       Case 86 this\ax=Cos(this\arg[0]);cos macro
	       Case 87 this\ax=CountChildren(this\arg[0]);countchildren macro
	       Case 88 this\ax=CountCollisions(this\arg[0]);countcollisions macro
	       Case 89 this\ax=CountGfxDrivers();countgfxdrivers macro
	       Case 90 this\ax=CountGfxModes();countgfxmodes macro
	       Case 91 this\ax=CountHostIPs(this\arg[0]);counthostips macro
	       Case 92 this\ax=CountSurfaces(this\arg[0]);countsurfaces macro
	       Case 93 this\ax=CountTriangles(this\arg[0]);counttriangles macro
	       Case 94 this\ax=CountVertices(this\arg[0]);countvertices macro
	       Case 95 this\ax=CreateBank(this\arg[0]);createbank macro
	       Case 96 this\ax=CreateBrush(this\arg[0],this\arg[1],this\arg[2]);createbrush macro
	       Case 97 this\ax=CreateCamera(this\arg[0]);createcamera macro
	       Case 98 this\ax=CreateCone(this\arg[0],this\arg[1],this\arg[2]);createcone macro
	       Case 99 this\ax=CreateCube(this\arg[0]);createcube macro
	       Case 100 this\ax=CreateCylinder(this\arg[0],this\arg[1],this\arg[2]);createcylinder macro
	       Case 101 CreateDir(this\arg[0])
	       Case 102 this\ax=CreateImage(this\arg[0],this\arg[1],this\arg[2]);createimage macro
	       Case 103 this\ax=CreateLight(this\arg[0],this\arg[1]);createlight macro
	       Case 104 this\ax=CreateListener(this\arg[0],this\arg[1],this\arg[2],this\arg[3]);createlistener macro
	       Case 105 this\ax=CreateMesh(this\arg[0]);createmesh macro
	       Case 106 this\ax=CreateNetPlayer(this\arg[0]);createnetplayer macro
	       Case 107 this\ax=CreatePivot(this\arg[0]);createpivot macro
	       Case 108 this\ax=CreatePlane(this\arg[0],this\arg[1]);createplane macro
	       Case 109 this\ax=CreateSphere(this\arg[0],this\arg[1]);createsphere macro
	       Case 110 this\ax=CreateSprite(this\arg[0]);createsprite macro
	       Case 111 this\ax=CreateSurface(this\arg[0],this\arg[1]);createsurface macro
	       Case 112 this\ax=CreateTCPServer(this\arg[0]);createtcpserver macro
	       Case 113 this\ax=CreateTerrain(this\arg[0],this\arg[1]);createterrain macro
	       Case 114 this\ax=CreateTimer(this\arg[0]);createtimer macro
	       Case 115 this\ax=CreateUDPStream(this\arg[0]);createudpstream macro
	       Case 116 this\ax=CurrentDate$();currentdate$ macro
	       Case 117 this\ax=CurrentDir$();currentdir$ macro
	       Case 118 this\ax=CurrentTime$();currenttime macro
	       Case 119 DebugLog(this\arg[0])
	       Case 120 Delay(this\arg[0])
	       Case 121 DeleteDir(this\arg[0])
	       Case 122 DeleteFile(this\arg[0])
	       Case 123 DeleteNetPlayer(this\arg[0])
	       Case 124 Dither(this\arg[0])
	       Case 125 this\ax=DottedIP$(ip);dottedip$ macro
	       Case 126 DrawBlock(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 127 DrawBlockRect(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7])
	       Case 128 DrawImage(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 129 DrawImageRect(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7])
	       Case 130 EmitSound(this\arg[0],this\arg[1])
	       Case 131 termination=True;terminate macro
	       Case 132 EntityAlpha(this\arg[0],this\arg[1])
	       Case 133 EntityAutoFade(this\arg[0],this\arg[1],this\arg[2])
	       Case 134 EntityBlend(this\arg[0],this\arg[1])
	       Case 135 EntityBox(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6])
	       Case 136 EntityCollided(this\arg[0],this\arg[1])
	       Case 137 EntityColor(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 138 EntityDistance#(this\arg[0],this\arg[1])
	       Case 139 EntityFX(this\arg[0],this\arg[1])
	       Case 140 EntityInView(this\arg[0],this\arg[1])
	       Case 141 this\ax=EntityName$(this\arg[0])
	       Case 142 EntityOrder(this\arg[0],this\arg[1])
	       Case 143 EntityParent(this\arg[0],this\arg[1],this\arg[2])
	       Case 144 EntityPick(this\arg[0],this\arg[1])
	       Case 145 EntityPickMode(this\arg[0],this\arg[1],this\arg[2])
	       Case 146 EntityPitch#(this\arg[0],this\arg[1])
	       Case 147 EntityRadius(this\arg[0],this\arg[1])
	       Case 148 EntityRoll#(this\arg[0],this\arg[1])
	       Case 149 EntityShininess(this\arg[0],this\arg[1])
	       Case 150 EntityTexture(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 151 EntityType(this\arg[0],this\arg[1],this\arg[2])
	       Case 152 EntityVisible(this\arg[0],this\arg[1])
	       Case 153 this\ax=EntityX#(this\arg[0],this\arg[1])
	       Case 154 this\ax=EntityY#(this\arg[0],this\arg[1])
	       Case 155 this\ax=EntityYaw#(this\arg[0],this\arg[1])
	       Case 156 this\ax=EntityZ#(this\arg[0],this\arg[1])
	       Case 157 this\ax=Eof(this\arg[0]);eof macro
	       Case 158 ExecFile(this\arg[0])
	       Case 159 this\ax=Exp(this\arg[0]);exp macro
	       Case 160 this\ax=FilePos(this\arg[0]);filepos macro
	       Case 161 this\ax=FileSize(this\arg[0]);filesize macro
	       Case 162 this\ax=FileType(this\arg[0]);filetype macro
	       Case 163 this\ax=FindChild(this\arg[0],this\arg[1]);findchild macro
	       Case 164 this\ax=FindSurface(this\arg[0],this\arg[1]);findsurface macro
	       Case 165 FitMesh(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7])
	       Case 166 Flip(this\arg[0])
	       Case 167 FlipMesh(this\arg[0])
	       Case 168 this\ax=Floor#(this\arg[0]);floor macro
	       Case 169 FlushJoy()
	       Case 170 FlushKeys()
	       Case 171 FlushMouse()
	       Case 172 this\ax=FontHeight()
	       Case 173 this\ax=FontWidth()
	       Case 174 FreeBank(this\arg[0])
	       Case 175 FreeBrush(this\arg[0])
	       Case 176 FreeEntity(this\arg[0])
	       Case 177 FreeFont(this\arg[0])
	       Case 178 FreeImage(this\arg[0])
	       Case 179 FreeSound(this\arg[0])
	       Case 180 FreeTexture(this\arg[0])
	       Case 181 FreeTimer(this\arg[0])
	       Case 182 FrontBuffer()
	       Case 183 this\ax=GetChild(this\arg[0],this\arg[1]);getchild macro
	       Case 184 GetColor(this\arg[0],this\arg[1])
	       Case 185 this\ax=GetEntityType(this\arg[0]);getentitytype macro
	       Case 186 this\ax=GetJoy(this\arg[0]);getjoy macro
	       Case 187 this\ax=GetKey();getkey macro
	       Case 188 this\ax=GetMouse();getmouse macro
	       Case 189 this\ax=GetParent(this\arg[0]);getparent macro
	       Case 190 this\ax=GetSurface(this\arg[0],this\arg[1]);getsurface macro
	       Case 191 this\ax=GfxDriver3D(this\arg[0]);gfxdriver3d macro
	       Case 192 this\ax=GfxDriverName$(this\arg[0]);gfxdrivername$ macro
	       Case 193 this\ax=GfxMode3D(this\arg[0]);gfxmode3d macro
	       Case 194 this\ax=GfxMode3DExists(this\arg[0],this\arg[1],this\arg[2]);gfxmode3dexists macro
	       Case 195 this\ax=GfxModeDepth(this\arg[0]);gfxmodedepth macro
	       Case 196 this\ax=GfxModeExists(this\arg[0],this\arg[1],this\arg[2]);gfxmodeexists macro
	       Case 197 this\ax=GfxModeHeight(this\arg[0]);gfxmodeheight macro
	       Case 198 this\ax=GfxModeWidth(this\arg[0]);gfxmodewidth macro
	       Case 199 GrabImage(this\arg[0],this\arg[1],this\arg[2],this\arg[3]); macro
	       Case 200 Graphics3D(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 201 this\ax=GraphicsBuffer()
	       Case 202 this\ax=GraphicsDepth();graphicsdepth macro
	       Case 203 this\ax=GraphicsHeight();graphicsheight macro
	       Case 204 this\ax=GraphicsWidth();graphicswidth macro
	       Case 205 HandleImage(this\arg[0],this\arg[1],this\arg[2])
	       Case 206 HandleSprite(this\arg[0],this\arg[1],this\arg[2])
	       Case 207 this\ax=Hex$(this\arg[0]);hex macro
	       Case 208 HideEntity(this\arg[0])
	       Case 209 HidePointer()
	       Case 210 HostIP(this\arg[0])
	       Case 211 HostNetGame(this\arg[0])
	       Case 212 HWMultiTex(this\arg[0])
	       Case 213 ImageBuffer (this\arg[0],this\arg[1])
	       Case 214 this\ax=ImageHeight(this\arg[0]);imageheight macro
	       Case 215 this\ax=ImageRectCollide(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7]);imagerectcollide macro
	       Case 216 this\ax=ImageRectOverlap(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6]);imagerectoverlap macro
	       Case 217 this\ax=ImagesCollide(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7]);imagescollide macro
	       Case 218 this\ax=ImagesOverlap(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5]);imagesoverlap macro
	       Case 219 this\ax=ImageWidth(this\arg[0]);imagewidth macro
	       Case 220 this\ax=ImageXHandle(this\arg[0]);imagexhandle macro
	       Case 221 this\ax=ImageYHandle(this\arg[0]);imageyhandle macro
	       Case 222 this\ax=Input(this\arg[0])
	       Case 223 this\ax=Instr(this\arg[0],this\arg[1],this\arg[2])
	       Case 224 JoinNetGame(this\arg[0],this\arg[1]);macro
	       Case 225 this\ax=JoyX(this\arg[0]);joyx macro
	       Case 226 this\ax=JoyXDir(this\arg[0]);joyxdir macro
	       Case 227 this\ax=JoyY(this\arg[0]);joyy macro
	       Case 228 this\ax=JoyYDir(this\arg[0]);joyydir macro
	       Case 229 this\ax=JoyZ(this\arg[0]);joyz macro
	       Case 230 this\ax=JoyZDir(this\arg[0]);joyzdir macro
	       Case 231 this\ax=KeyDown(this\arg[0]);keydown macro
	       Case 232 this\ax=KeyHit(this\arg[0]);keyhit macro
	       Case 233 this\ax=Left(this\arg[0],this\arg[1]);left$ macro
	       Case 234 this\ax=Len(this\arg[0]);len macro
	       Case 235 LightColor(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 236 LightMesh(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7])
	       Case 237 LightRange(this\arg[0],this\arg[1])
	       Case 238 Line(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 239 LinePick(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6])
	       Case 240 this\ax=Load3DSound(this\arg[0]);load3dsound macro
	       Case 241 this\ax=LoadAnimImage(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4]);loadanimimage macro
	       Case 242 this\ax=LoadAnimMesh(this\arg[0],this\arg[1]);loadanimmesh macro
	       Case 243 this\ax=LoadAnimSeq(this\arg[0],this\arg[1]);loadanimseq macro
	       Case 244 this\ax=LoadAnimTexture(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5]);loadanimtexture macro
	       Case 245 this\ax=LoadBrush(this\arg[0],this\arg[1],this\arg[2],this\arg[3]);loadbrush macro
	       Case 246 this\ax=LoadBSP(this\arg[0],this\arg[1],this\arg[2]);loadbsp macro
	       Case 247 this\ax=LoadBuffer(this\arg[0],this\arg[1]);loadbuffer macro
	       Case 248 LoaderMatrix(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7],this\arg[8],this\arg[9])
	       Case 249 this\ax=LoadFont(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4]);loadfont macro
	       Case 250 this\ax=LoadImage(this\arg[0]);loadimage macro
	       Case 251 this\ax=LoadMD2(this\arg[0],this\arg[1]);loadmd2 macro
	       Case 252 this\ax=LoadMesh(this\arg[0],this\arg[1]);loadmesh macro
	       Case 253 this\ax=LoadSound(this\arg[0]);loadsound macro
	       Case 254 this\ax=LoadSprite(this\arg[0],this\arg[1],this\arg[2]);loadsprite macro
	       Case 255 this\ax=LoadTerrain(this\arg[0],this\arg[1]);loadterrain macro
	       Case 256 this\ax=LoadTexture(this\arg[0],this\arg[1]);loadtexture macro
	       Case 257 LockBuffer(this\arg[0])
	       Case 258 this\ax=Log(this\arg[0]);log macro
	       Case 259 this\ax=Log10(this\arg[0]);log10 macro
	       Case 260 LoopSound(this\arg[0])
	       Case 261 Lower$(this\arg[0])
	       Case 262 LSet$(this\arg[0],this\arg[1])
	       Case 263 MaskImage(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 264 MD2Animating(this\arg[0])
	       Case 265 MD2AnimLength(this\arg[0])
	       Case 266 MD2AnimTime(this\arg[0])
	       Case 267 this\ax=MeshDepth#(this\arg[0])
	       Case 268 this\ax=MeshesIntersect(this\arg[0],this\arg[1])
	       Case 269 this\ax=MeshHeight#(this\arg[0])
	       Case 270 this\ax=MeshWidth#(this\arg[0])
	       Case 271 this\ax=Mid$(this\arg[0],this\arg[1],this\arg[2]); macro
	       Case 272 MidHandle(this\arg[0])
	       Case 273 this\ax=MilliSecs();millisecs macro
	       Case 274 ModifyTerrain(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 275 this\ax=MouseDown(this\arg[0]);mousedown macro
	       Case 276 this\ax=MouseHit(this\arg[0]);mousehit macro
	       Case 277 this\ax=MouseX();mousex macro 
	       Case 278 this\ax=MouseXSpeed();mousexspeed macro
	       Case 279 this\ax=MouseY();mousey macro
	       Case 280 this\ax=MouseYSpeed();mouseyspeed macro
	       Case 281 this\ax=MouseZ();mousez macro
	       Case 282 this\ax=MouseZSpeed();mousezspeed macro
	       Case 283 MoveEntity(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 284 MoveMouse(this\arg[0],this\arg[1])
	       Case 285 NameEntity(this\arg[0],this\arg[1])
	       Case 286 this\ax=NetMsgData$()
	       Case 287 this\ax=NetMsgFrom()
	       Case 288 this\ax=NetMsgTo()
	       Case 289 this\ax=NetMsgType()
	       Case 290 NetPlayerLocal(this\arg[0])
	       Case 291 this\ax=NetPlayerName$(this\arg[0])
	       Case 292 NextFile$(this\arg[0])
	       Case 293 OpenFile(this\arg[0])
	       Case 294 OpenTCPStream(this\arg[0],this\arg[1])
	       Case 295 Origin(this\arg[0],this\arg[1])
	       Case 296 Oval(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 297 PaintEntity(this\arg[0],this\arg[1])
	       Case 298 PaintMesh(this\arg[0],this\arg[1])
	       Case 299 PaintSurface(this\arg[0],this\arg[1])
	       Case 300 PauseChannel(this\arg[0])
	       Case 301 this\ax=PeekByte(this\arg[0],this\arg[1])
	       Case 302 this\ax=PeekFloat(this\arg[0],this\arg[1])
	       Case 303 this\ax=PeekInt(this\arg[0],this\arg[1])
	       Case 304 this\ax=PeekShort(this\arg[0],this\arg[1])
	       Case 305 this\ax=PickedEntity();pickedentity macro
	       Case 306 this\ax=PickedNX#();pickednx macro
	       Case 307 this\ax=PickedNY#();pickedny macro
	       Case 308 this\ax=PickedNZ#();pickednz macro
	       Case 309 this\ax=PickedSurface();pickedsurface macro
	       Case 310 this\ax=PickedTime();pickedtime macro
	       Case 311 this\ax=PickedTriangle();pickedtriangle macro
	       Case 312 this\ax=PickedX#();pickedx macro
	       Case 313 this\ax=PickedY#();pickedy macro
	       Case 314 this\ax=PickedZ#();pickedz macro
	       Case 315 PlayCDTrack(this\arg[0],this\arg[1])
	       Case 316 PlayMusic(this\arg[0])
	       Case 317 PlaySound(this\arg[0])
	       Case 318 Plot(this\arg[0],this\arg[1])
	       Case 319 PointEntity(this\arg[0],this\arg[1],this\arg[2])
	       Case 320 PokeByte(this\arg[0],this\arg[1],this\arg[2])
	       Case 321 PokeFloat(this\arg[0],this\arg[1],this\arg[2])
	       Case 322 PokeInt(this\arg[0],this\arg[1],this\arg[2])
	       Case 323 PokeShort(this\arg[0],this\arg[1],this\arg[2])
	       Case 324 PositionEntity(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 325 PositionMesh(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 326 PositionTexture(this\arg[0],this\arg[1],this\arg[2])
	       Case 327 Print(this\arg[0])
	       Case 328 this\ax=ProjectedX#();projectedx macro
	       Case 329 this\ax=ProjectedY#();projectedy macro
	       Case 330 this\ax=ProjectedZ#();projectedz macro
	       Case 331 Rand(this\arg[0],this\arg[1])
	       Case 332 this\ax=ReadAvail(this\arg[0]);readavail macro
	       Case 333 this\ax=ReadByte(this\arg[0]);readbyte macro
	       Case 334 this\ax=ReadBytes(this\arg[0],this\arg[1],this\arg[2],this\arg[3]);readbytes macro
	       Case 335 this\ax=ReadDir(this\arg[0]);readdir macro
	       Case 336 this\ax=ReadFile(this\arg[0]);readfile macro
	       Case 337 this\ax=ReadFloat(this\arg[0]);readfloat macro
	       Case 338 this\ax=ReadInt(this\arg[0]);readint macro
	       Case 339 this\ax=ReadLine$(this\arg[0]);readline$ macro
	       Case 340 this\ax=ReadPixel(this\arg[0],this\arg[1],this\arg[2]);readpixel macro
	       Case 341 this\ax=ReadPixelFast(this\arg[0],this\arg[1],this\arg[2]);readpixelfast macro
	       Case 342 this\ax=ReadShort(this\arg[0]);readshort macro
	       Case 343 this\ax=ReadString$(this\arg[0]);readstring$ macro
	       Case 344 Rect(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 345 RectsOverlap(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5],this\arg[6],this\arg[7])
	       Case 346 this\ax=RecvNetMsg()
	       Case 347 this\ax=RecvUDPMsg(this\arg[0])
	       Case 348 Replace$(this\arg[0],this\arg[1],this\arg[2])
	       Case 349 ResetEntity(this\arg[0])
	       Case 350 ResizeBank(this\arg[0],this\arg[1])
	       Case 351 ResizeImage(this\arg[0],this\arg[1],this\arg[2])
	       Case 352 ResumeChannel(this\arg[0])
	       Case 353 this\ax=Right$(this\arg[0],this\arg[1]);right$ macro
	       Case 354 this\ax=Rnd(this\arg[0],this\arg[1]);rnd macro
	       Case 355 RotateEntity(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 356 RotateImage(this\arg[0],this\arg[1])
	       Case 357 RotateMesh(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 358 RotateSprite(this\arg[0],this\arg[1])
	       Case 359 RotateTexture(this\arg[0],this\arg[1])
	       Case 360 RSet$(this\arg[0],this\arg[1])
	       Case 361 RuntimeError(this\arg[0])
	       Case 362 SaveBuffer(this\arg[0],this\arg[1])
	       Case 363 SaveImage(this\arg[0],this\arg[1],this\arg[2])
	       Case 364 ScaleEntity(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 365 ScaleImage(this\arg[0],this\arg[1],this\arg[2])
	       Case 366 ScaleMesh(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 367 ScaleSprite(this\arg[0],this\arg[1],this\arg[2])
	       Case 368 ScaleTexture(this\arg[0],this\arg[1],this\arg[2])
	       Case 369 this\ax=ScanLine()
	       Case 370 SeedRnd(this\arg[0])
	       Case 371 SeekFile(this\arg[0],this\arg[1])
	       Case 372 SendNetMsg(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 373 SendUDPMsg(this\arg[0],this\arg[1],this\arg[2])
	       Case 374 SetAnimKey(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 375 SetFont(this\arg[0])
	       Case 376 SetGfxDriver(this\arg[0])
	       Case 377 ShowEntity(this\arg[0])
	       Case 378 ShowPointer()
	       Case 379  this\ax=Sin(this\arg[0])
	       Case 380 SoundPan(this\arg[0],this\arg[1])
	       Case 381 SoundPitch(this\arg[0],this\arg[1])
	       Case 382 SoundVolume(this\arg[0],this\arg[1])
	       Case 383 SpriteViewMode(this\arg[0],this\arg[1])
	       Case 384 this\ax=Sqr(this\arg[0]);sqr macro
	       Case 385 this\ax=StartNetGame()
	       Case 386 Stop()
	       Case 387 StopChannel(this\arg[0])
	       Case 388 StopNetGame()
	       Case 389 String$(this\arg[0],this\arg[1])
	       Case 390 StringHeight(this\arg[0])
	       Case 391 StringWidth(this\arg[0])
	       Case 392 SystemProperty(this\arg[0])
	       Case 393 Tan(this\arg[0])
	       Case 394 TCPStreamIP(this\arg[0])
	       Case 395 TCPStreamPort(this\arg[0])
	       Case 396 TCPTimeouts(this\arg[0],this\arg[1])
	       Case 397 TerrainDetail(this\arg[0],this\arg[1],this\arg[2])
	       Case 398 TerrainHeight#(this\arg[0],this\arg[1],this\arg[2])
	       Case 399 TerrainShading(this\arg[0],this\arg[1])
	       Case 400 this\ax=TerrainSize(this\arg[0])
	       Case 401 TerrainX#(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 402 TerrainY#(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 403 TerrainZ#(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 404 Text(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 405 TextureBlend(this\arg[0],this\arg[1])
	       Case 406 TextureBuffer(this\arg[0],this\arg[1])
	       Case 407 TextureCoords(this\arg[0],this\arg[1])
	       Case 408 TextureFilter(this\arg[0],this\arg[1])
	       Case 409 this\ax=TextureHeight(texture);textureheight macro
	       Case 410 this\ax=TextureWidth(texture);texturewidth macro
	       Case 411 this\ax=TFormedX();tformedx macro
	       Case 412 this\ax=TFormedY();tformedy macro
	       Case 413 this\ax=TFormedZ();tformedz macro
	       Case 414 TFormFilter(this\arg[0])
	       Case 415 TFormImage(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 416 TFormNormal(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 417 TFormPoint(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 418 TFormVector(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 419 TileBlock(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 420 TileImage(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 421 this\ax=TotalVidMem();totalvidmem macro
	       Case 422 TranslateEntity(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 423 TriangleVertex(this\arg[0],this\arg[1],this\arg[2])
	       Case 424 Trim$(this\arg[0])
	       Case 425 this\ax=TrisRendered()
	       Case 426 TurnEntity(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 427 UDPMsgIP(this\arg[0])
	       Case 428 UDPMsgPort(this\arg[0])
	       Case 429 UDPStreamIP(this\arg[0])
	       Case 430 UDPStreamPort(this\arg[0])
	       Case 431 UDPTimeouts(this\arg[0])
	       Case 432 UnlockBuffer(this\arg[0])
	       Case 433 UpdateNormals(this\arg[0])
	       Case 434 UpdateWorld(this\arg[0])
	       Case 435 Upper$(this\arg[0])
	       Case 436 VertexBlue#(this\arg[0],this\arg[1])
	       Case 437 VertexColor(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 438 VertexCoords(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 439 VertexGreen#(this\arg[0],this\arg[1])
	       Case 440 VertexNormal(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4])
	       Case 441 VertexNX#(this\arg[0],this\arg[1])
	       Case 442 VertexNY#(this\arg[0],this\arg[1])
	       Case 443 VertexNZ#(this\arg[0],this\arg[1])
	       Case 444 VertexRed#(this\arg[0],this\arg[1])
	       Case 445 VertexTexCoords(this\arg[0],this\arg[1],this\arg[2],this\arg[3],this\arg[4],this\arg[5])
	       Case 446 VertexU#(this\arg[0],this\arg[1])
	       Case 447 VertexV#(this\arg[0],this\arg[1])
	       Case 448 VertexW#(this\arg[0],this\arg[1])
	       Case 449 VertexX#(this\arg[0],this\arg[1])
	       Case 450 VertexY#(this\arg[0],this\arg[1])
	       Case 451 VertexZ#(this\arg[0],this\arg[1])
	       Case 452 Viewport(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 453 VWait(this\arg[0])
	       Case 454 WaitJoy(this\arg[0])
	       Case 455 WaitKey()
	       Case 456 WaitMouse()
	       Case 457 WaitTimer(this\arg[0])
	       Case 458 WBuffer(this\arg[0])
	       Case 459 Windowed3D()
	       Case 460 WireFrame(this\arg[0])
	       Case 461 Write(this\arg[0])
	       Case 462 WriteByte(this\arg[0],this\arg[1])
	       Case 463 WriteBytes(this\arg[0],this\arg[1],this\arg[2],this\arg[3])
	       Case 464 WriteFile(this\arg[0])
	       Case 465 WriteFloat(this\arg[0],this\arg[1])
	       Case 466 WriteInt(this\arg[0],this\arg[1])
	       Case 467 WriteLine(this\arg[0],this\arg[1])
	       Case 468 WritePixel(this\arg[0],this\arg[1],this\arg[0],this\arg[0])
	       Case 469 WritePixelFast(this\arg[0],this\arg[1],this\arg[0],this\arg[0])
	       Case 470 WriteShort(this\arg[0],this\arg[1])
	       Case 471 WriteString(this\arg[0],this\arg[1])
	       Case 472 RenderWorld(this\arg[0])
	       Case 473 rndseed()
	       
	       ;BSCVM Calls ====================================
	       Case 474 bscvmThreadLoad(this\arg[0])
	       Case 475 bscvmThreadExec(this\arg[0])
	       Case 476 bscvmThreadRun(this\arg[0],this\arg[1])
	       Case 477 bscvmThreadHalt(this\arg[0])
	       Case 478 bscvmThreadKill(this\arg[0])
	       Case 479 this\ax=bscvmThreadRunning(this\arg[0])
	       Case 480 bscvmThreadSchedule(this\arg[0],this\arg[1])
	       Case 481 blitzscriptVarSet(this\arg[0],this\arg[1])
	       Case 482 this\ax=blitzscriptVarGet(this\arg[0])
	       
	       ;ENGINE Calls ====================================
	       
	       ;GUI
      
	End Select
End Function