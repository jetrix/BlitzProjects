;============================
;BSCVMCOMPILER MODULE
;Generated with TypeWriter][
;============================
;BlitzScript3D Compiler  by Frankie Taylor
;Statements supported: let, print, end, if, then, else
;Operators supported: binary +, -, *, /, <, =, >, <=, <>, >= and unary -
;String Operators supported: binary @ or $+, $*, $<, =, $>, $<=, <>, $=>
;<<,>>,^,*, / have precedence over +, -, which have precendence over <,=,>,<=,<>,>=
;Parenthesis can be used to group subexpressions.
;http://www.csn.ul.ie/~darkstar/assembler/tut3.html

;*.bsc - BlitzScript3D Code File
;*.bso - BlitzScript3D Object (PCode) File

Const BSCVMCOMPILER_MAX%=32

Const	BSCVM_COMPILER_END = -1

Const	BSCVM_TOKEN_LET	=	1
Const	BSCVM_TOKEN_VAR	=	2
Const	BSCVM_TOKEN_VALUE	=	3
Const	BSCVM_TOKEN_STRING	=	4
Const	BSCVM_TOKEN_ARRAY	=	5
Const	BSCVM_TOKEN_ARRAYSTRING	=	6
Const	BSCVM_TOKEN_ARRAYCOMMA	=	7
Const	BSCVM_TOKEN_FUNCTION	=	8
Const	BSCVM_TOKEN_FUNCTIONCOMMA	=	9
Const 	BSCVM_TOKEN_FUNCTIONRETURN	=	10
Const	BSCVM_TOKEN_ENDFUNCTION	=	11
Const	BSCVM_TOKEN_SCRIPT	=	12
Const	BSCVM_TOKEN_ENDSCRIPT	=	13
Const	BSCVM_TOKEN_FOR	=	14
Const	BSCVM_TOKEN_TO	=	15
Const	BSCVM_TOKEN_STEP	=	16
Const	BSCVM_TOKEN_NEXT	=	17
Const	BSCVM_TOKEN_WHILE	=	18
Const	BSCVM_TOKEN_ENDWHILE	=	19
Const	BSCVM_TOKEN_EXIT	=	20
Const	BSCVM_TOKEN_IF	=	21
Const	BSCVM_TOKEN_THEN	=	22
Const	BSCVM_TOKEN_ELSE	=	23
Const	BSCVM_TOKEN_ENDIF	=	24
Const	BSCVM_TOKEN_SELECT	=	25
Const	BSCVM_TOKEN_CASE	=	26
Const	BSCVM_TOKEN_DEFAULTCASE	=	27
Const	BSCVM_TOKEN_ENDSELECT	=	28
Const	BSCVM_TOKEN_OPENBRACKET	=	29
Const	BSCVM_TOKEN_CLOSEBRACKET	=	30
Const	BSCVM_TOKEN_OPENPAR	=	31
Const	BSCVM_TOKEN_CLOSEPAR	=	32
Const	BSCVM_TOKEN_OPENQUOTE	=	33
Const	BSCVM_TOKEN_CLOSEQUOTE	=	34
Const	BSCVM_TOKEN_OPENTAG	=	35
Const	BSCVM_TOKEN_CLOSETAG	=	36
Const	BSCVM_TOKEN_OPENCOMMENT	=	37
Const	BSCVM_TOKEN_CLOSECOMMENT	=	38
Const	BSCVM_TOKEN_MUL	=	39
Const	BSCVM_TOKEN_SHL	=	40
Const	BSCVM_TOKEN_POW	=	41
Const	BSCVM_TOKEN_DIV	=	42
Const	BSCVM_TOKEN_SHR	=	43
Const	BSCVM_TOKEN_ADD	=	44
Const	BSCVM_TOKEN_INC	=	45
Const	BSCVM_TOKEN_SUB	=	46
Const	BSCVM_TOKEN_DEC	=	47
Const	BSCVM_TOKEN_EQ	=	48
Const	BSCVM_TOKEN_NE	=	49
Const	BSCVM_TOKEN_GT	=	50
Const	BSCVM_TOKEN_GE	=	51
Const	BSCVM_TOKEN_GT_STR	=	66
Const	BSCVM_TOKEN_GE_STR	=	67
Const	BSCVM_TOKEN_LT_STR	=	68
Const	BSCVM_TOKEN_LE_STR	=	69
Const	BSCVM_TOKEN_MUL_STR	=	70
Const	BSCVM_TOKEN_LT	=	52
Const	BSCVM_TOKEN_LE	=	53
Const	BSCVM_TOKEN_END	=	54
Const	BSCVM_TOKEN_COLON	=	55
Const	BSCVM_TOKEN_INCLUDE	=	56
Const	BSCVM_TOKEN_PUSH	=	57
Const	BSCVM_TOKEN_POP	=	58
Const	BSCVM_TOKEN_GOTO	=	59
Const	BSCVM_TOKEN_OBJECT	=	60
Const	BSCVM_TOKEN_OBJECTPROPERTY	=	61
Const	BSCVM_TOKEN_ENDOBJECT =	62
Const	BSCVM_TOKEN_METHOD	=	63
Const	BSCVM_TOKEN_CONCENATE	=	64
Const	BSCVM_TOKEN_ENDMETHOD	=	65

Const	BSCVM_TOKEN_CALL_MASK	=	71	;ALways Last token, value must always be greater than any other tokens

Dim bscvmCompilerId.bscvmCompiler(BSCVMCOMPILER_MAX%)

Global bscvmCompilerIndex.stack=stackIndexCreate(BSCVMCOMPILER_MAX%)
Global BSCVM_TOKEN_CALL

Type bscvmCompiler
	;Purpose:
	Field id%
	Field token$
	Field line$
	Field line0$
	Field linex%
	Field liney%
	Field msg$
	Field label%
	Field filename$
	Field inputfile%
	Field outputfile%
	Field instructionPTR%
End Type

Function bscvmCompilerStart(bscvmInputFilename$)
	;Purpose: 
	;Parameters:
	;Return:
	;load bscvmCallIndex List
	bscvmCompilerLoad(bscvmInputFilename$)
End Function

Function bscvmCompilerLoad(bscvmInputFilename$)
	;Purpose: Compiles *.bsc (BlitzScript3D Code) Files to *.bso (BlitzScript3D PCode Obj) Files
	;Parameters: bscvmInputFilename - *.bsc filename
	;Return: None
	this.bscvmCompiler=bscvmCompilerNew()
	DebugLog ";compiling..."
	this\filename$=bscvmInputFilename$
	this\InputFile%=ReadFile(this\filename$+".bsc")
	If Not this\InputFile% 
		bscvmCompilerSyntaxError( this,this\filename$+".bsc Not Found")
		End
	EndIf
	this\OutputFile%=WriteFile(this\filename$+".bso")
	bscvmtoken=bscvmCompilerTokenGet(this)
	Repeat
		DebugLog ";"+this\Token$+this\Line$
		bscvmtoken=bscvmCompilerParseStatement(this,bscvmtoken)
	Until bscvmtoken=BSCVM_COMPILER_END
End Function

Function bscvmCompilerStop()
	;Purpose:
	;Parameters:
	;Return:
	For this.bscvmCompiler=Each bscvmCompiler
		bscvmCompilerDelete(this)
	Next
End Function

Function bscvmCompilerNew.bscvmCompiler()
	;Purpose: Creates a new bscvmCompiler instance
	;Parameters: none
	;Return: bscvmCompiler Object
	this.bscvmCompiler=New bscvmCompiler
	this\id%=0
	this\token$=""
	this\line$=""
	this\linex%=0
	this\liney%=0
	this\msg$=""
	this\label%=0
	this\filename$=""
	this\inputfile%=0
	this\outputfile%=0
	this\id%=StackPop(bscvmCompilerIndex.stack)
	bscvmCompilerId(this\id%)=this
	Return this
End Function

Function bscvmCompilerDelete(this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	bscvmCompilerId(this\id)=Null
	StackPush(bscvmCompilerIndex.stack,this\id%)
	this\filename$=""
	this\msg$=""
	this\line$=""
	this\token$=""
	Delete this
End Function

Function bscvmCompilerUpdate()
	;Purpose:
	;Parameters:
	;Return:
	For this.bscvmCompiler=Each bscvmCompiler
	Next
End Function

Function bscvmCompilerRead.bscvmCompiler(file)
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmCompiler=New bscvmCompiler
	this\id%=ReadInt(file)
	this\token$=ReadLine(file)
	this\line$=ReadLine(file)
	this\linex%=ReadInt(file)
	this\liney%=ReadInt(file)
	this\msg$=ReadLine(file)
	this\label%=ReadInt(file)
	this\filename$=ReadLine(file)
	this\inputfile%=ReadInt(file)
	this\outputfile%=ReadInt(file)
	Return this
End Function

Function bscvmCompilerWrite(file,this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	WriteInt(file,this\id%)
	WriteLine(file,this\token$)
	WriteLine(file,this\line$)
	WriteInt(file,this\linex%)
	WriteInt(file,this\liney%)
	WriteLine(file,this\msg$)
	WriteInt(file,this\label%)
	WriteLine(file,this\filename$)
	WriteInt(file,this\inputfile%)
	WriteInt(file,this\outputfile%)
End Function

Function bscvmCompilerOpen(filename$="Default")
	;Purpose:
	;Parameters:
	;Return:
	file=ReadFile(filename+".bscvmCompiler")
	Repeat
		bscvmCompilerRead(file)
	Until Eof(file)
	CloseFile(file)
End Function

Function bscvmCompilerSave(filename$="Default")
	;Purpose:
	;Parameters:
	;Return:
	file=WriteFile(filename$+".bscvmCompiler")
	For this.bscvmCompiler= Each bscvmCompiler
		bscvmCompilerWrite(file,this)
	Next
	CloseFile(file)
End Function

Function bscvmCompilerCopy.bscvmCompiler(this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	copy.bscvmCompiler=New bscvmCompiler
	copy\id%=this\id%
	copy\token$=this\token$
	copy\line$=this\line$
	copy\linex%=this\linex%
	copy\liney%=this\liney%
	copy\msg$=this\msg$
	copy\label%=this\label%
	copy\filename$=this\filename$
	copy\inputfile%=this\inputfile%
	copy\outputfile%=this\outputfile%
	copy\id%=StackPop(bscvmCompilerIndex.stack)
	bscvmCompilerId(copy\id%)=copy
	Return copy
End Function

Function bscvmCompilerMimic(mimic.bscvmCompiler,this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	mimic\id%=this\id%
	mimic\token$=this\token$
	mimic\line$=this\line$
	mimic\linex%=this\linex%
	mimic\liney%=this\liney%
	mimic\msg$=this\msg$
	mimic\label%=this\label%
	mimic\filename$=this\filename$
	mimic\inputfile%=this\inputfile%
	mimic\outputfile%=this\outputfile%
End Function

Function bscvmCompilerCreate.bscvmCompiler(id%,token$,Line$,linex%,liney%,msg$,label%,filename$,inputfile%,outputfile%)
	;Purpose:
	;Parameters:
	;Return:
	this.bscvmCompiler=bscvmCompilerNew()
	this\id%=id%
	this\token$=token$
	this\line$=line$
	this\linex%=linex%
	this\liney%=liney%
	this\msg$=msg$
	this\label%=label%
	this\filename$=filename$
	this\inputfile%=inputfile%
	this\outputfile%=outputfile%
	Return this
End Function

Function bscvmCompilerSet(this.bscvmCompiler,id%,token$,Line$,linex%,liney%,msg$,label%,filename$,inputfile%,outputfile%)
	;Purpose:
	;Parameters:
	;Return:
	this\id%=id%
	this\token$=token$
	this\line$=line$
	this\linex%=linex%
	this\liney%=liney%
	this\msg$=msg$
	this\label%=label%
	this\filename$=filename$
	this\inputfile%=inputfile%
	this\outputfile%=outputfile%
End Function

Function bscvmCompilerParseStatement(this.bscvmCompiler, bscvmtoken) ;pass bscvmtoken$
	;Purpose:
	;Parameters:
	;Return:

	Select bscvmtoken
		Case BSCVM_TOKEN_VAR, BSCVM_TOKEN_LET
			If bscvmtoken=BSCVM_TOKEN_LET
				If bscvmCompilerTokenGet(this)<>BSCVM_TOKEN_VAR bscvmCompilerSyntaxError(this,"Unrecognize Variable")
			EndIf	
			id$=this\token$
			bscvmVar.bscvmVar=bscvmVarCreate( this\token$ )
			If bscvmCompilerTokenGet(this)<>BSCVM_TOKEN_EQ bscvmCompilerSyntaxError(this,"Expected '='")
			bscvmtoken=bscvmCompilerParseExpression(this)

			DebugLog "BSCVM_OP_MOV_VAR_EAX {"+id$+":"+bscvmVar\id%+"}"		
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MOV_VAR_EAX)
			bscvmCompilerWriteOutputFile(this,bscvmVar\id%)
				
		Case BSCVM_TOKEN_IF
			If bscvmCompilerParseExpression(this)<>BSCVM_TOKEN_THEN bscvmCompilerSyntaxError(this,"Expected THEN Keyword")
			this\label=this\label+1
			label=this\label

			DebugLog "BSCVM_OP_JZ {"+label+"}"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_JZ)
			bscvmCompilerWriteOutputFile(this,label)	

			bscvmtoken=bscvmCompilerParseStatement(this,bscvmCompilerTokenGet(this))
			If bscvmtoken=BSCVM_TOKEN_ELSE
				this\label=this\label+1
				label2=this\label
			
				DebugLog "BSCVM_OP_JMP {"+label2+"}"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_JMP)
				bscvmCompilerWriteOutputFile(this,label2)
						
				DebugLog "BSCVM_OP_LABEL {"+label+"}"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_LABEL)
				bscvmLabelCreate(label,this\instructionPTR)
				;bscvmCompilerWriteOutputFile(this,label)
				;bscvmCompilerWriteOutputFile(this,this\instructionPTR)
				
				bscvmtoken=bscvmCompilerParseStatement(this,bscvmCompilerTokenGet(this) )
				label=label2
			EndIf
			
			DebugLog "BSCVM_OP_LABEL {"+label+"}"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_LABEL)
			bscvmLabelCreate(label,this\instructionPTR)
			;bscvmCompilerWriteOutputFile(this,label)
			;bscvmCompilerWriteOutputFile(this,this\instructionPTR)

		Case BSCVM_TOKEN_SELECT
			
		Case BSCVM_TOKEN_FOR
		
		Case BSCVM_TOKEN_WHILE
						
		Case BSCVM_TOKEN_CALL
			DebugLog "BSCVM_OP_PUSH_CALL {"+Str(BSCVM_TOKEN_CALL-BSCVM_TOKEN_CALL_MASK)+"}"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_CALL)
			bscvmCompilerWriteOutputFile(this,BSCVM_TOKEN_CALL-BSCVM_TOKEN_CALL_MASK)
			
			If bscvmCompilerTokenGet(this)<>BSCVM_TOKEN_OPENPAR bscvmCompilerSyntaxError(this,"Expected '('")
			bscvmtoken=bscvmCompilerParseArguments(this)
			DebugLog "BSCVM_OP_POP_CALL"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_CALL)
		
		Case BSCVM_TOKEN_FUNCTION
			;unconditional branch to sub routine that push instruction ptr value to stack
			DebugLog "BSCVM_OP_BSR {"+Str(BSCVM_TOKEN_BSR)+"}"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_BSR)
			
			bscvmtoken=bscvmCompilerParseExpression(this)		
				
		Case BSCVM_TOKEN_END
			;bscvmtoken$=bscvmCompilerParseStatement( bscvmCompilerTokenGet(this) )

			DebugLog "BSCVM_COMPILER_END {"+this\instructionPTR+"}"

			For bscvmVar.bscvmVar = Each bscvmVar
				DebugLog "BSCVM_OP_NEWVAR {"+bscvmVar\id%+"}"
				WriteLine(this\outputFile%,BSCVM_OP_NEWVAR)
				WriteLine(this\outputFile%,bscvmVar\id%)
			Next
			
			For bscvmLabel.bscvmLabel = Each bscvmLabel
				DebugLog "BSCVM_OP_NEWLABEL {"+bscvmLabel\id%+"}"
				WriteLine(this\outputFile%,BSCVM_OP_NEWLABEL)
				WriteLine(this\outputFile%,bscvmLabel\id%)
				WriteLine(this\outputFile%,bscvmLabel\offset%)
			Next			
			
;			For bscvmBSR.bscvmBSR = Each bscvmBSR
;				DebugLog "BSCVM_OP_NEWBSR {"+bscvmBSR\id%+"}"
;			Next			
			
			CloseFile(this\InputFile%)
			CloseFile(this\outputFile%)
			
			bscvmtoken=BSCVM_COMPILER_END
			
		Default
			bscvmCompilerSyntaxError(this,"Unrecognizable Statement")
			
		End Select
	Return bscvmtoken
End Function

Function bscvmCompilerParseExpression(this.bscvmCompiler) 
	;Purpose:
	;Parameters:
	;Return:
	bscvmtoken=bscvmCompilerParseTerm(this)
	Repeat
	
		Select bscvmtoken
			Case BSCVM_TOKEN_LT bscvmOp$="slt"
			Case BSCVM_TOKEN_GT bscvmOp$="sgt"
			Case BSCVM_TOKEN_LE bscvmOp$="sle"
			Case BSCVM_TOKEN_GE bscvmOp$="sge"
			Case BSCVM_TOKEN_LT_STR bscvmOp$="$slt"
			Case BSCVM_TOKEN_GT_STR bscvmOp$="$sgt"
			Case BSCVM_TOKEN_LE_STR bscvmOp$="$sle"
			Case BSCVM_TOKEN_GE_STR bscvmOp$="$sge"			
			Case BSCVM_TOKEN_EQ bscvmOp$="seq"
			Case BSCVM_TOKEN_NE bscvmOp$="sne"
			Default Return bscvmtoken
		End Select
		
		DebugLog "BSCVM_OP_PUSH_EAX"
		bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
		
		bscvmtoken=bscvmCompilerParseTerm(this)
		
;		DebugLog "BSCVM_OP_POP_ECX"
;		bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)
				
		DebugLog "BSCVM_OP_"+Upper(bscvmOp$)+"_EAX"   
		Select bscvmOp$
			Case "slt" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SLT_EAX)
			Case "sgt" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SGT_EAX)
			Case "sle" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SLE_EAX)
			Case "sge" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SGE_EAX)
			Case "$slt" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SLT_STR)
			Case "$sgt" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SGT_STR)
			Case "$sle" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SLE_STR)
			Case "$sge" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SGE_STR)
			Case "seq" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SEQ_EAX)
			Case "sne" bscvmCompilerWriteOutputFile(this,BSCVM_OP_SNE_EAX)												
		End Select		

	Forever
End Function                                 

Function bscvmCompilerParseTerm(this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	bscvmtoken=bscvmCompilerParseFact(this)
	Repeat
		Select bscvmtoken
		
			Case BSCVM_TOKEN_CONCENATE
				DebugLog "BSCVM_OP_PUSH_EAX"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)			
				
				bscvmtoken=bscvmCompilerParseFact(this)
				
				;reduced instruction set BSCVM_OP_POP_ECX_ADD_EAX_ECX
				
;				DebugLog "BSCVM_OP_POP_ECX"
;				bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)	
						
				DebugLog "BSCVM_OP_CONCENATE_EAX_ECX"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_CONCENATE_EAX_ECX)
						
			Case BSCVM_TOKEN_ADD
				DebugLog "BSCVM_OP_PUSH_EAX"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)			
				
				bscvmtoken=bscvmCompilerParseFact(this)
				
				;reduced instruction set BSCVM_OP_POP_ECX_ADD_EAX_ECX
				
;				DebugLog "BSCVM_OP_POP_ECX"
;				bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)	
						
				DebugLog "BSCVM_OP_ADD_EAX_ECX"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_ADD_EAX_ECX)
	
			Case BSCVM_TOKEN_SUB
				DebugLog "BSCVM_OP_PUSH_EAX"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
				
				bscvmtoken=bscvmCompilerParseFact(this)
	
				;reduced instruction BSCVM_OP_MOV_ECX_EAX_POP_EAX_SUB_EAX_ECX
				
;				DebugLog "BSCVM_OP_MOV_ECX_EAX"
;				bscvmCompilerWriteOutputFile(this,BSCVM_OP_MOV_ECX_EAX)
;				
;				DebugLog "BSCVM_OP_POP_EAX"
;				bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_EAX)
				
				DebugLog "BSCVM_OP_SUB_EAX_ECX"
				bscvmCompilerWriteOutputFile(this,BSCVM_OP_SUB_EAX_ECX)
				
			Default
				Return bscvmtoken
		End Select
	Forever
End Function

Function bscvmCompilerParseFact(this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	bscvmtoken=bscvmCompilerParseLeaf(this)
	Repeat
		Select bscvmtoken
		Case BSCVM_TOKEN_MUL
			DebugLog "BSCVM_OP_PUSH_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
			
			bscvmtoken=bscvmCompilerParseLeaf(this)
			
			;reduced instruction: BSCVM_OP_POP_ECX_MUL_EAX_ECX
			
;			DebugLog "BSCVM_OP_POP_ECX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)			

			DebugLog "BSCVM_OP_MUL_EAX_ECX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MUL_EAX_ECX)	

		Case BSCVM_TOKEN_SHL
			DebugLog "BSCVM_OP_PUSH_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
			
			bscvmtoken=bscvmCompilerParseLeaf(this)
			
			;reduced instruction: ,BSCVM_OP_POP_ECX_SHR_EAX_ECX
			
;			DebugLog "BSCVM_OP_POP_ECX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)			

			DebugLog "BSCVM_OP_SHL_EAX_ECX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_SHL_EAX_ECX)

		Case BSCVM_TOKEN_POW
			DebugLog "BSCVM_OP_PUSH_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)

			bscvmtoken=bscvmCompilerParseLeaf(this)
			
			;reduced instruction:  BSCVM_OP_POP_ECX_POW_EAX_ECX
			
;			DebugLog "BSCVM_OP_POP_ECX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)			

			DebugLog "BSCVM_OP_POW_EAX_ECX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POW_EAX_ECX)

		Case BSCVM_TOKEN_MUL_STR
			DebugLog "BSCVM_OP_PUSH_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
			
			bscvmtoken=bscvmCompilerParseLeaf(this)
			
			;reduced instruction: BSCVM_OP_POP_ECX_MUL_EAX_ECX
			
;			DebugLog "BSCVM_OP_POP_ECX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)			

			DebugLog "BSCVM_OP_MUL_STR"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MUL_STR)							
			
		Case BSCVM_TOKEN_DIV
		
			DebugLog "BSCVM_OP_PUSH_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
						
			bscvmtoken=bscvmCompilerParseLeaf(this)
			
			;reduced instruction: BSCVM_OP_MOV_ECX_EAX_POP_ECX_DIV_EAX_ECX
			
;			DebugLog "BSCVM_OP_MOV_ECX_EAX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MOV_ECX_EAX)
						
;			DebugLog "BSCVM_OP_POP_ECX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)
						
			DebugLog "BSCVM_OP_DIV_EAX_ECX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_DIV_EAX_ECX)	

		Case BSCVM_TOKEN_SHR
		
			DebugLog "BSCVM_OP_PUSH_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_EAX)
						
			bscvmtoken=bscvmCompilerParseLeaf(this)
			
			;reduced instruction:  BSCVM_OP_MOV_ECX_EAX_POP_ECX_SHR_EAX_ECX
			
;			DebugLog "BSCVM_OP_MOV_ECX_EAX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MOV_ECX_EAX)
						
;			DebugLog "BSCVM_OP_POP_ECX"
;			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ECX)
						
			DebugLog "BSCVM_OP_SHR_EAX_ECX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_SHR_EAX_ECX)		

		Default
			Return bscvmtoken
		End Select
	Forever
End Function

Function bscvmCompilerParseLeaf(this.bscvmCompiler)
	;Purpose:
	;Parameters:
	;Return:
	bscvmtoken=bscvmCompilerTokenGet(this)
	Select bscvmtoken
		Case BSCVM_TOKEN_SUB
			bscvmtoken=bscvmCompilerParseLeaf(this)
			DebugLog "BSCVM_OP_NEG_EAX"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_NEG_EAX)		
			
		Case BSCVM_TOKEN_OPENPAR
			If bscvmCompilerParseExpression(this) <> BSCVM_TOKEN_CLOSEPAR bscvmCompilerSyntaxError(this,"Expected Closing ')'")
			bscvmtoken=bscvmCompilerTokenGet(this)
			
		Case BSCVM_TOKEN_CLOSEPAR	
	
		Case BSCVM_TOKEN_VAR
			bscvmVar.bscvmVar=bscvmVarCreate( this\token$ )
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MOV_EAX_VAR)		
			bscvmCompilerWriteOutputFile(this,bscvmVar\id%)		
			
			DebugLog "BSCVM_OP_MOV_EAX_VAR {"+this\token$+":"+bscvmVar\id%+"}"
	
			bscvmtoken=bscvmCompilerTokenGet(this)
	
		Case BSCVM_TOKEN_VALUE
			DebugLog "BSCVM_OP_MOV_EAX_VALUE {"+this\token$+"}"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_MOV_EAX_VALUE)
			bscvmCompilerWriteOutputFile(this,this\token$)
	
			bscvmtoken=bscvmCompilerTokenGet(this)
	
		Case BSCVM_TOKEN_CALL
			DebugLog "BSCVM_OP_PUSH_CALL {"+Str(BSCVM_TOKEN_CALL-BSCVM_TOKEN_CALL_MASK)+"}"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_CALL)
			bscvmCompilerWriteOutputFile(this,BSCVM_TOKEN_CALL-BSCVM_TOKEN_CALL_MASK)
			If bscvmCompilerTokenGet(this)<>BSCVM_TOKEN_OPENPAR bscvmCompilerSyntaxError(this,"Expected '('")
			bscvmtoken=bscvmCompilerParseArguments(this)
			DebugLog "BSCVM_OP_POP_CALL"
			bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_CALL)
			
		Default
			bscvmCompilerSyntaxError(this,"Expected Variable or Value")
	End Select
	Return bscvmtoken
End Function

Function bscvmCompilerParseArguments(this.bscvmCompiler)
	Repeat
		bscvmtoken=bscvmCompilerParseExpression(this);bscvmCompilerTokenGet(this);
		Select bscvmtoken

			Case BSCVM_TOKEN_COMMA
			
				Select this\token$
					Default
					
						DebugLog "BSCVM_OP_PUSH_ARG {"+bscvmargs+"}"
						bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_ARG)	
						bscvmargs=bscvmargs+1
						
				End Select
				
			Case BSCVM_TOKEN_CLOSEPAR	
				;If bscvmargs
				
					DebugLog "BSCVM_OP_PUSH_ARG {"+bscvmargs+"}"
					bscvmCompilerWriteOutputFile(this,BSCVM_OP_PUSH_ARG)
									
					For bscvmloop = bscvmargs To 0 Step -1
						DebugLog "BSCVM_OP_POP_ARG {"+bscvmloop+"}"
						bscvmCompilerWriteOutputFile(this,BSCVM_OP_POP_ARG)
						bscvmCompilerWriteOutputFile(this,bscvmloop)
					Next
				;EndIf
				Return bscvmCompilerTokenGet(this)
	
			Default
				bscvmCompilerSyntaxError(this,"Invalid Argument")
		End Select
	Forever
End Function

Function bscvmCompilerParseFunction(this.bscvmCompiler)
End Function

Function bscvmCompilerSyntaxError(this.bscvmCompiler,msg$="Syntax Error")
	;Purpose: Generates a Runtime Error
	;Parameters:
	;	this.bscvmCompiler - bscvmCompiler Object
	;	msg$ - Syntax Error Message String
	;Return: None

	bscvmlinelen=Len(this\token$) 
	bscvmlinech=this\lineX-bscvmlinelen 
	msgleft$=Mid(this\Line0,1,bscvmlinech) 
	msgright$=Mid(this\Line0,Len(msgleft)+bscvmlinelen+1,Len(this\Line0));
	
;	Graphics(640,480,16,2)
;	color(255,0,0)
;	Print("Syntax Error: " + msg$)
;	color(255,255,255)
;	Print("File:"+this\filename$+".bsc Line:" + this\lineY + ", Chr:" +str((this\lineX-bscvmlinelen)+1)); 
;	Print(this\line0)
;	color(255,255,0)
;	Write(msgleft)
;	color(255,0,0)
;	Write("##" + this\token$ + "##")
;	color(255,255,0)
;	Print( msgright )	
;	color(0,255,255)
;	print("bscvmCompiler will now terminate.")
;	waitkey()
;	end
	
	this\msg="Syntax Error: "+msg$+Chr(13)+"File:"+this\filename$+".bsc, Line:" + this\lineY+", Chr:"+Str( (this\lineX-bscvmlinelen)+1)+Chr(13)+msgleft+"##"+ this\token$+"##"+msgright	
	RuntimeError(this\msg)
End Function                    
	
Function bscvmCompilerChrGet$(this.bscvmCompiler)
	;Purpose: Reads lines from *.bsc input file
	;Parameters: bscvmCompiler Object
	;Return: the first left character of the input  file line string read
	If this\Line$=""
		this\Line$=ReadLine(this\InputFile%) +" "
		this\line0$=this\line
		If this\Line$=" " And Eof(this\inputfile)=True this\Line$="end "
		this\lineY%=this\lineY%+1 ;syntax error checking vertical Line (Ln) position within the input file.
		this\lineX%=reset%
	EndIf
	Return Left( this\Line$,1 )
End Function

Function bscvmCompilerChrSet(this.bscvmCompiler)
	;Purpose:  Reduces input file line string read by 1 from the left
	;Parameters: bscvmCompiler Object 
	;Return: None
	this\line$=Mid$( this\line$,2 )
	this\lineX%=this\lineX%+1 ;syntax error checking horizontal character (Ch) position within the line.
End Function

Function bscvmCompilerTokenGet(this.bscvmCompiler)
	;Purpose: Parses Token from *.bsc input file
	;Parameters: bscvmCompiler Object
	;Return: Token Value (see Const List above for applicable Tokens)

	Repeat
		this\token$=bscvmCompilerChrGet$(this)
		bscvmCompilerChrSet(this)
	Until this\token$<>" " 
	
	;Numerical Value
	If Instr("0123456789.",this\token$)
		Repeat
			bscvmtoken$=bscvmCompilerChrGet$(this)
			If Not Instr("0123456789.",bscvmtoken$) Return BSCVM_TOKEN_VALUE
			this\token$=this\token$+bscvmtoken$
			bscvmCompilerChrSet(this)
		Forever
	EndIf
	
	;String Value
	If this\token$=Chr(34);Open Quote
		this\token$=nil$
		For bscvmloop = 0 To 255
			bscvmtoken$=bscvmCompilerChrGet$(this)
			If bscvmtoken$=Chr(34);Close Quote
				bscvmCompilerChrSet(this.bscvmCompiler)
				Return BSCVM_TOKEN_VALUE
			endIf	
			this\token$=this\token$+bscvmtoken$
			bscvmCompilerChrSet(this)
		Next
	EndIf	
	
	;Keywords
	If Instr("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_",this\token)
		Repeat
			bscvmtoken$=bscvmCompilerChrGet$(this)
			If Not Instr("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_",bscvmtoken$)
				Select Lower(this\token$)
					Case "let" Return BSCVM_TOKEN_LET
					Case "function" Return BSCVM_TOKEN_LET
					Case "while" Return BSCVM_TOKEN_WHILE
					Case "for" Return BSCVM_TOKEN_FOR
					Case "to" Return BSCVM_TOKEN_TO
					Case "step" Return BSCVM_TOKEN_STEP
					Case "next" Return BSCVM_TOKEN_NEXT
					Case "select" Return  BSCVM_TOKEN_SELECT 
					Case "if" Return BSCVM_TOKEN_IF
					Case "then" Return BSCVM_TOKEN_THEN
					Case "else" Return BSCVM_TOKEN_ELSE
					Case "rem" Return BSCVM_TOKEN_COMMENT
					Case "end" Return BSCVM_TOKEN_END
					Case "endif" Return BSCVM_TOKEN_ENDIF
					Case "endfunction" Return BSCVM_TOKEN_ENDFUNCTION
					Case "endrem" Return BSCVM_TOKEN_ENDCOMMENT
					Case "endscript" Return BSCVM_TOKEN_ENDSCRIPT
					Case "endselect" Return BSCVM_TOKEN_ENDSELECT
					Case "endtype" Return BSCVM_TOKEN_ENDTYPE
					Case "endwhile" Return BSCVM_TOKEN_ENDWHILE
					Case "object" Return BSCVM_TOKEN_OBJECT
					Case "property" Return BSCVM_TOKEN_PROPERTY
					Case "endobject" Return BSCVM_TOKEN_ENDOBJECT
					Case "method" Return BSCVM_TOKEN_METHOD
					Case "endmethod" Return BSCVM_TOKEN_ENDMETHOD
					
					Default 
						BSCVM_TOKEN_CALL = bscvmCompilerCallIndex(this)+BSCVM_TOKEN_CALL_MASK
						If BSCVM_TOKEN_CALL > BSCVM_TOKEN_CALL_MASK Return BSCVM_TOKEN_CALL
						
						;check for bsr label. if found Return BSCVM_TOKEN_BSR						
						
				End Select
				;Variables
				Return BSCVM_TOKEN_VAR
			EndIf
			this\token$=this\token$+bscvmtoken$
			bscvmCompilerChrSet(this)
		Forever
	EndIf
	
	Select this\token$
	
		Case ";" Return BSCVM_TOKEN_COMMENT
		Case "," Return BSCVM_TOKEN_COMMA
		Case ":" Return BSCVM_TOKEN_COLON
		Case "[" Return BSCVM_TOKEN_OPENBRACKET
		Case "]" Return BSCVM_TOKEN_CLOSEBRACKET
		Case "(" Return BSCVM_TOKEN_OPENPAR
		Case ")" Return BSCVM_TOKEN_CLOSEPAR

		Case "+" Return BSCVM_TOKEN_ADD
		Case "@" Return BSCVM_TOKEN_CONCENATE
 		Case "-" Return BSCVM_TOKEN_SUB
		Case "*" Return BSCVM_TOKEN_MUL
		Case "^" Return BSCVM_TOKEN_POW
		Case "/" Return BSCVM_TOKEN_DIV
		Case "="
			If bscvmCompilerChrGet$(this)="<" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_LE
			EndIf	
			If bscvmCompilerChrGet$(this)=">" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_GE
			EndIf	
			Return BSCVM_TOKEN_EQ
			
		Case "<"
			If bscvmCompilerChrGet$(this)="=" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_LE
			EndIf	
			If bscvmCompilerChrGet$(this)=">" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_NE
			EndIf	
			If bscvmCompilerChrGet$(this)="<" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_SHL
			EndIf			
			Return BSCVM_TOKEN_LT
			
		Case ">"
			If bscvmCompilerChrGet$(this)="=" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_GE
			EndIf	
			If bscvmCompilerChrGet$(this)="<" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_NE
			EndIf	
			If bscvmCompilerChrGet$(this)=">" 
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_SHR
			EndIf			
			Return BSCVM_TOKEN_GT

		Case "$"
			If bscvmCompilerChrGet$(this)="+"
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_CONCENATE
			EndIf
			If bscvmCompilerChrGet$(this)="*"
				bscvmCompilerChrSet(this)
				Return BSCVM_TOKEN_MUL_STR
			EndIf
			
			If bscvmCompilerChrGet$(this)=">"
				bscvmCompilerChrSet(this)
				If bscvmCompilerChrGet$(this)="="
					bscvmCompilerChrSet(this)
					Return BSCVM_TOKEN_GE_STR
				End If 					
				Return BSCVM_TOKEN_GT_STR
			EndIf
			
			If bscvmCompilerChrGet$(this)="<"
				bscvmCompilerChrSet(this)
				If bscvmCompilerChrGet$(this)="="
					bscvmCompilerChrSet(this)
					Return BSCVM_TOKEN_LE_STR
				EndIf
				If bscvmCompilerChrGet$(this)=">"
					bscvmCompilerChrSet(this)
					Return BSCVM_TOKEN_NEQ
				EndIf					
				Return BSCVM_TOKEN_LT_STR
			EndIf
			
			If bscvmCompilerChrGet$(this)="="
				bscvmCompilerChrSet(this)
				If bscvmCompilerChrGet$(this)=">"
					bscvmCompilerChrSet(this)
					Return BSCVM_TOKEN_GE_STR
				End If 					
				Return BSCVM_TOKEN_EQ
			EndIf
			If bscvmCompilerChrGet$(this)="="
				bscvmCompilerChrSet(this)
				If bscvmCompilerChrGet$(this)="<"
					bscvmCompilerChrSet(this)
					Return BSCVM_TOKEN_EQ
				EndIf	
				Return BSCVM_TOKEN_LT_STR
			EndIf						
										
	End Select
	
	bscvmCompilerSyntaxError(this,"Unrecognized Expression")
	
End Function

Function bscvmCompilerWriteOutputFile(this.bscvmCompiler,bscvmValue$)
	;Purpose: Writes PCode to *.bso outputfile and tracks instruction pointer
	;Parameters:
	;	this.bscvmCompiler - Compiler Object
	;	bscvmValue$ - value to write to outputfile
	;Return:
	WriteLine(this\outputfile%,bscvmValue$)
	this\instructionPTR=this\instructionPTR+1
End Function 

Function bscvmCompilerCallIndex(this.bscvmCompiler)
	;Purpose: Compiler Function Call Tree. 	The Select Case Values are the Function  Labels 
	;	recognized by the Compiler. The return values correspond to the Function Index
	;	called in the bscvmThreadCall Function. When  adding new functions to be 
	;	recognized by the Compiler ,  you add a Function Label (lowercase) statement to 
	;	this select tree. 
	
	;	Example:	Case "your_function_label" Return bscvmThreadCall_Index
	;				Case "botScan" Return 512	
	
	;	Note: Maximum Number of Function Parameters = 16
	
	;Parameters: bscvmCompiler Object
	;Return: bscvmThreadCall_Index
	
	Select Lower(this\token$)		
	
		;BLITZ Calls ====================================		
		Case "accepttcpstream" Return 1
		Case "acos" Return 2
		Case "addanimseq" Return 3
		Case "addmesh" Return 4
		Case "addtriangle" Return 5
		Case "addvertex" Return 6
		Case "aligntovector" Return 7
		Case "ambientlight" Return 8
		Case "animate" Return 9
		Case "animatemd2" Return 10
		Case "animating" Return 11
		Case "animlength" Return 12
		Case "animseq" Return 13
		Case "animtime" Return 14
		Case "antialias" Return 15
		Case "apptitle" Return 16
		Case "asc" Return 17
		Case "asin" Return 18
		Case "atan" Return 19
		Case "atan2" Return 20
		Case "automidhandle" Return 21
		Case "availvidmem" Return 22
		Case "backbuffer" Return 23
		Case "banksize" Return 24
		Case "bin" Return 25
		Case "brushalpha" Return 26
		Case "brushblend" Return 27
		Case "brushfx" Return 28
		Case "brushshininess" Return 29
		Case "brushtexture" Return 30
		Case "bspambientlight" Return 31
		Case "bsplighting" Return 32
		Case "cameraclscolor" Return 33
		Case "cameraclsmode" Return 34
		Case "camerafogcolor" Return 35
		Case "camerafogmode" Return 36
		Case "camerafogrange" Return 37
		Case "camerapick" Return 38
		Case "cameraproject" Return 39
		Case "camerarange" Return 40
		Case "cameraviewport" Return 41
		Case "camerazoom" Return 42
		Case "captureworld" Return 43
		Case "ceil" Return 44
		Case "changedir" Return 45
		Case "channelpan" Return 46
		Case "channelpitch" Return 47
		Case "channelplaying" Return 48
		Case "channelvolume" Return 49
		Case "chr" Return 50
		Case "clearcollisions" Return 51
		Case "clearsurface" Return 52
		Case "cleartexturefilters" Return 53
		Case "clearworld" Return 54
		Case "closedir" Return 55
		Case "closefile" Return 56
		Case "closetcpserver" Return 57
		Case "closetcpstream" Return 58
		Case "closeudpstream" Return 59
		Case "cls" Return 60
		Case "clscolor" Return 61
		Case "collisionentity" Return 62
		Case "collisionnx" Return 63
		Case "collisionny" Return 64
		Case "collisionnz" Return 65
		Case "collisions" Return 66
		Case "collisionsurface" Return 67
		Case "collisiontime" Return 68
		Case "collisiontriangle" Return 69
		Case "collisionx" Return 70
		Case "collisiony" Return 71
		Case "collisionz" Return 72
		Case "color" Return 73
		Case "colorblue" Return 74
		Case "colorgreen" Return 75
		Case "colorred" Return 76
		Case "commandline" Return 77
		Case "copybank" Return 78
		Case "copyentity" Return 79
		Case "copyfile" Return 80
		Case "copyimage" Return 81
		Case "copypixel" Return 82
		Case "copypixelfast" Return 83
		Case "copyrect" Return 84
		Case "copystream" Return 85
		Case "cos" Return 86
		Case "countchildren" Return 87
		Case "countcollisions" Return 88
		Case "countgfxdrivers","graphicsdrivercount" Return 89
		Case "countgfxmodes" Return 90
		Case "counthostips" Return 91
		Case "countsurfaces" Return 92
		Case "counttriangles" Return 93
		Case "countvertices" Return 94
		Case "createbank" Return 95
		Case "createbrush" Return 96
		Case "createcamera" Return 97
		Case "createcone" Return 98
		Case "createcube" Return 99
		Case "createcylinder" Return 100
		Case "createdir" Return 101
		Case "createimage" Return 102
		Case "createlight" Return 103
		Case "createlistener" Return 104
		Case "createmesh" Return 105
		Case "createnetplayer" Return 106
		Case "createpivot" Return 107
		Case "createplane" Return 108
		Case "createsphere" Return 109
		Case "createsprite" Return 110
		Case "createsurface" Return 111
		Case "createtcpserver" Return 112
		Case "createterrain" Return 113
		Case "createtimer" Return 114
		Case "createudpstream" Return 115
		Case "currentdate" Return 116
		Case "currentdir" Return 117
		Case "currenttime" Return 118
		Case "debuglog" Return 119
		Case "delay" Return 120
		Case "deletedir" Return 121
		Case "deletefile" Return 122
		Case "deletenetplayer" Return 123
		Case "dither" Return 124
		Case "dottedip" Return 125
		Case "drawblock" Return 126
		Case "drawblockrect" Return 127
		Case "drawimage" Return 128
		Case "drawimagerect" Return 129
		Case "emitsound" Return 130
		Case "end" Return 131
		Case "entityalpha" Return 132
		Case "entityautofade" Return 133
		Case "entityblend" Return 134
		Case "entitybox" Return 135
		Case "entitycollided" Return 136
		Case "entitycolor" Return 137
		Case "entitydistance" Return 138
		Case "entityfx" Return 139
		Case "entityinview" Return 140
		Case "entityname" Return 141
		Case "entityorder" Return 142
		Case "entityparent" Return 143
		Case "entitypick" Return 144
		Case "entitypickmode" Return 145
		Case "entitypitch" Return 146
		Case "entityradius" Return 147
		Case "entityroll" Return 148
		Case "entityshininess" Return 149
		Case "entitytexture" Return 150
		Case "entitytype" Return 151
		Case "entityvisible" Return 152
		Case "entityx" Return 153
		Case "entityy" Return 154
		Case "entityyaw" Return 155
		Case "entityz" Return 156
		Case "eof" Return 157
		Case "execfile" Return 158
		Case "exp" Return 159
		Case "filepos" Return 160
		Case "filesize" Return 161
		Case "filetype" Return 162
		Case "findchild" Return 163
		Case "findsurface" Return 164
		Case "fitmesh" Return 165
		Case "flip" Return 166
		Case "flipmesh" Return 167
		Case "floor" Return 168
		Case "flushjoy" Return 169
		Case "flushkeys" Return 170
		Case "flushmouse" Return 171
		Case "fontheight" Return 172
		Case "fontwidth" Return 173
		Case "freebank" Return 174
		Case "freebrush" Return 175
		Case "freeentity","entityfree" Return 176
		Case "freefont" Return 177
		Case "freeimage" Return 178
		Case "freesound" Return 179
		Case "freetexture" Return 180
		Case "freetimer" Return 181
		Case "frontbuffer" Return 182
		Case "getchild" Return 183
		Case "getcolor" Return 184
		Case "getentitytype" Return 185
		Case "getjoy" Return 186
		Case "getkey" Return 187
		Case "getmouse" Return 188
		Case "getparent" Return 189
		Case "getsurface" Return 190
		Case "gfxdriver3d","graphicsdriver3d" Return 191
		Case "gfxdrivername","graphicsdrivername" Return 192
		Case "gfxmode3d","graphicsmode3d" Return 193
		Case "gfxmode3dexists","graphicsmode3dexists" Return 194
		Case "gfxmodedepth","graphicsmodedepth" Return 195
		Case "gfxmodeexists","graphicsmodeexists" Return 196
		Case "gfxmodeheight","graphicsmodeheight" Return 197
		Case "gfxmodewidth","graphicsmodewidth" Return 198
		Case "grabimage" Return 199
		Case "graphics3d" Return 200
		Case "graphicsbuffer" Return 201
		Case "graphicsdepth" Return 202
		Case "graphicsheight" Return 203
		Case "graphicswidth" Return 204
		Case "handleimage" Return 205
		Case "handlesprite" Return 206
		Case "hex" Return 207
		Case "hideentity","entityhide" Return 208
		Case "hidepointer" Return 209
		Case "hostip" Return 210
		Case "hostnetgame" Return 211
		Case "hwmultitex" Return 212
		Case "imagebuffer" Return 213
		Case "imageheight" Return 214
		Case "imagerectcollide" Return 215
		Case "imagerectoverlap" Return 216
		Case "imagescollide" Return 217
		Case "imagesoverlap" Return 218
		Case "imagewidth" Return 219
		Case "imagexhandle" Return 220
		Case "imageyhandle" Return 221
		Case "input" Return 222
		Case "instr" Return 223
		Case "joinnetgame" Return 224
		Case "joyx" Return 225
		Case "joyxdir" Return 226
		Case "joyy" Return 227
		Case "joyydir" Return 228
		Case "joyz" Return 229
		Case "joyzdir" Return 230
		Case "keydown" Return 231
		Case "keyhit" Return 232
		Case "left" Return 233
		Case "len" Return 234
		Case "lightcolor" Return 235
		Case "lightmesh" Return 236
		Case "lightrange" Return 237
		Case "line" Return 238
		Case "linepick" Return 239
		Case "load3dsound" Return 240
		Case "loadanimimage" Return 241
		Case "loadanimmesh" Return 242
		Case "loadanimseq" Return 243
		Case "loadanimtexture" Return 244
		Case "loadbrush" Return 245
		Case "loadbsp" Return 246
		Case "loadbuffer" Return 247
		Case "loadermatrix" Return 248
		Case "loadfont","fontload" Return 249
		Case "loadimage" Return 250
		Case "loadmd2" Return 251
		Case "loadmesh" Return 252
		Case "loadsound" Return 253
		Case "loadsprite" Return 254
		Case "loadterrain" Return 255
		Case "loadtexture" Return 256
		Case "lockbuffer" Return 257
		Case "log" Return 258
		Case "log10" Return 259
		Case "loopsound" Return 260
		Case "lower" Return 261
		Case "lset" Return 262
		Case "maskimage" Return 263
		Case "md2animating" Return 264
		Case "md2animlength" Return 265
		Case "md2animtime" Return 266
		Case "meshdepth" Return 267
		Case "meshesintersect" Return 268
		Case "meshheight" Return 269
		Case "meshwidth" Return 270
		Case "mid" Return 271
		Case "midhandle" Return 272
		Case "millisecs" Return 273
		Case "modifyterrain" Return 274
		Case "mousedown" Return 275
		Case "mousehit" Return 276
		Case "mousex" Return 277
		Case "mousexspeed" Return 278
		Case "mousey" Return 279
		Case "mouseyspeed" Return 280
		Case "mousez" Return 281
		Case "mousezspeed" Return 282
		Case "moveentity" Return 283
		Case "movemouse" Return 284
		Case "nameentity" Return 285
		Case "netmsgdata" Return 286
		Case "netmsgfrom" Return 287
		Case "netmsgto" Return 288
		Case "netmsgtype" Return 289
		Case "netplayerlocal" Return 290
		Case "netplayername" Return 291
		Case "nextfile" Return 292
		Case "openfile" Return 293
		Case "opentcpstream" Return 294
		Case "origin" Return 295
		Case "oval" Return 296
		Case "paintentity" Return 297
		Case "paintmesh" Return 298
		Case "paintsurface" Return 299
		Case "pausechannel" Return 300
		Case "peekbyte" Return 301
		Case "peekfloat" Return 302
		Case "peekint" Return 303
		Case "peekshort" Return 304
		Case "pickedentity" Return 305
		Case "pickednx" Return 306
		Case "pickedny" Return 307
		Case "pickednz" Return 308
		Case "pickedsurface" Return 309
		Case "pickedtime" Return 310
		Case "pickedtriangle" Return 311
		Case "pickedx" Return 312
		Case "pickedy" Return 313
		Case "pickedz" Return 314
		Case "playcdtrack" Return 315
		Case "playmusic" Return 316
		Case "playsound" Return 317
		Case "plot" Return 318
		Case "pointentity" Return 319
		Case "pokebyte" Return 320
		Case "pokefloat" Return 321
		Case "pokeint" Return 322
		Case "pokeshort" Return 323
		Case "positionentity","entityposition" Return 324
		Case "positionmesh" Return 325
		Case "positiontexture" Return 326
		Case "print" Return 327
		Case "projectedx" Return 328
		Case "projectedy" Return 329
		Case "projectedz" Return 330
		Case "rand" Return 331
		Case "readavail" Return 332
		Case "readbyte" Return 333
		Case "readbytes" Return 334
		Case "readdir" Return 335
		Case "readfile" Return 336
		Case "readfloat" Return 337
		Case "readint" Return 338
		Case "readline" Return 339
		Case "readpixel" Return 340
		Case "readpixelfast" Return 341
		Case "readshort" Return 342
		Case "readstring" Return 343
		Case "rect" Return 344
		Case "rectsoverlap" Return 345
		Case "recvnetmsg" Return 346
		Case "recvudpmsg" Return 347
		Case "replace" Return 348
		Case "resetentity" Return 349
		Case "resizebank" Return 350
		Case "resizeimage" Return 351
		Case "resumechannel" Return 352
		Case "right" Return 353
		Case "rnd" Return 354
		Case "rotateentity","entityrotate" Return 355
		Case "rotateimage" Return 356
		Case "rotatemesh" Return 357
		Case "rotatesprite" Return 358
		Case "rotatetexture" Return 359
		Case "rset" Return 360
		Case "runtimeerror" Return 361
		Case "savebuffer" Return 362
		Case "saveimage" Return 363
		Case "scaleentity" Return 364
		Case "scaleimage" Return 365
		Case "scalemesh" Return 366
		Case "scalesprite" Return 367
		Case "scaletexture" Return 368
		Case "scanline" Return 369
		Case "seedrnd" Return 370
		Case "seekfile" Return 371
		Case "sendnetmsg" Return 372
		Case "sendudpmsg" Return 373
		Case "setanimkey" Return 374
		Case "setfont","fontset" Return 375
		Case "setgfxdriver","graphicsdriverset" Return 376
		Case "showentity","entityshow" Return 377
		Case "showpointer" Return 378
		Case "sin" Return 379
		Case "soundpan" Return 380
		Case "soundpitch" Return 381
		Case "soundvolume" Return 382
		Case "spriteviewmode" Return 383
		Case "sqr" Return 384
		Case "startnetgame" Return 385
		Case "stop" Return 386
		Case "stopchannel" Return 387
		Case "stopnetgame" Return 388
		Case "string" Return 389
		Case "stringheight" Return 390
		Case "stringwidth" Return 391
		Case "systemproperty" Return 392
		Case "tan" Return 393
		Case "tcpstreamip" Return 394
		Case "tcpstreamport" Return 395
		Case "tcptimeouts" Return 396
		Case "terraindetail" Return 397
		Case "terrainheight" Return 398
		Case "terrainshading" Return 399
		Case "terrainsize" Return 400
		Case "terrainx" Return 401
		Case "terrainy" Return 402
		Case "terrainz" Return 403
		Case "text" Return 404
		Case "textureblend" Return 405
		Case "texturebuffer" Return 406
		Case "texturecoords" Return 407
		Case "texturefilter" Return 408
		Case "textureheight" Return 409
		Case "texturewidth" Return 410
		Case "tformedx" Return 411
		Case "tformedy" Return 412
		Case "tformedz" Return 413
		Case "tformfilter" Return 414
		Case "tformimage" Return 415
		Case "tformnormal" Return 416
		Case "tformpoint" Return 417
		Case "tformvector" Return 418
		Case "tileblock" Return 419
		Case "tileimage" Return 420
		Case "totalvidmem" Return 421
		Case "translateentity","entitytranslate" Return 422
		Case "trianglevertex" Return 423
		Case "trim" Return 424
		Case "trisrendered" Return 425
		Case "turnentity","entityturn" Return 426
		Case "udpmsgip" Return 427
		Case "udpmsgport" Return 428
		Case "udpstreamip" Return 429
		Case "udpstreamport" Return 430
		Case "udptimeouts" Return 431
		Case "unlockbuffer" Return 432
		Case "updatenormals" Return 433
		Case "updateworld" Return 434
		Case "upper" Return 435
		Case "vertexblue" Return 436
		Case "vertexcolor" Return 437
		Case "vertexcoords" Return 438
		Case "vertexgreen" Return 439
		Case "vertexnormal" Return 440
		Case "vertexnx" Return 441
		Case "vertexny" Return 442
		Case "vertexnz" Return 443
		Case "vertexred" Return 444
		Case "vertextexcoords" Return 445
		Case "vertexu" Return 446
		Case "vertexv" Return 447
		Case "vertexw" Return 448
		Case "vertexx" Return 449
		Case "vertexy" Return 450
		Case "vertexz" Return 451
		Case "viewport" Return 452
		Case "vwait" Return 453
		Case "waitjoy" Return 454
		Case "waitkey" Return 455
		Case "waitmouse" Return 456
		Case "waittimer" Return 457
		Case "wbuffer" Return 458
		Case "windowed3d" Return 459
		Case "wireframe" Return 460
		Case "write" Return 461
		Case "writebyte" Return 462
		Case "writebytes" Return 463
		Case "writefile" Return 464
		Case "writefloat" Return 465
		Case "writeint" Return 466
		Case "writeline" Return 467
		Case "writepixel" Return 468
		Case "writepixelfast" Return 469
		Case "writeshort" Return 470
		Case "writestring" Return 471
		Case "renderworld" Return 472
		Case "rndseed" Return 473
		
		;BSCVM Calls ====================================
		Case "blitzscriptload"  Return 474 
		Case "blitzscriptexec"  Return 475
		Case "blitzscriptrun"  Return 476 
		Case "blitzscripthalt"  Return 477 
		Case "blitzscriptkill"  Return 478
		Case "blitzscriptrunning"  Return 479
		Case "blitzscriptschedule" Return 480
		Case "blitzscriptvarset" Return 481
		Case "blitzscriptvarget" Return 482
	       
		;ENGINE Calls ====================================
		
		;GUI
		
	End Select
				
End Function