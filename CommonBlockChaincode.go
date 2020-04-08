package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
	"fmt"
	"encoding/json"
	"bytes"
)

/**
通用
 */


const DOC_TYPE = "obj" //存数数据的类型

//参数类：对应Java里面的对象
type CommonBlockChaincode struct {
}

//信息结构体
type BlockDataEntity struct {
	Id    string    `json:"id"`     //主键
	Type	string	`json:"type"`	//数据类型：后勤、就业 ...
	Content    string    `json:"content"`  //数据内容
	CreatedDate string `json:"createdDate"`	//创建时间

	ObjectType	string	`json:"docType"`
	Historys    []HistoryItem    // 历史记录
}

//历史记录
type HistoryItem struct {
	TxId      string
	BlockDataEntity BlockDataEntity
}

//合约必要的init函数
func (t *CommonBlockChaincode) Init(stub shim.ChaincodeStubInterface) peer.Response{
	return shim.Success(nil)
}

//合约必要的invoke函数
func (t *CommonBlockChaincode) Invoke(stub shim.ChaincodeStubInterface) peer.Response{
	// 获取调用的方法名、方法参数
	fun, args := stub.GetFunctionAndParameters()

	if fun == "add"{
		return t.add(stub, args)        // 添加信息
	}else if fun == "queryInfoByIdAndTypeAndDate" {
		return t.queryInfoByIdAndTypeAndDate(stub, args)  //通过id + type + 创建时间查询【定位单个块】
	}else if fun == "queryInfoByIdAndType" {
		return t.queryInfoByIdAndType(stub, args)   // 根据主键及信息类型查询详情【溯源】
	}else if fun == "update" {
		return t.update(stub, args) // 根据主键更新信息
	}else if fun == "queryByType" {
		return t.queryByType(stub,args)	//根据type查询某一类信息
	}else if fun == "queryUniqueEdu" {
		return t.queryUnique(stub,args) 	//查询指定的区块信息【一个数据结果】
	}
	return shim.Error("指定的函数名称错误")
}

//程序入口
func main(){
	err := shim.Start(new(CommonBlockChaincode))
	if err != nil{
		fmt.Printf("启动通用链码时发生错误: %s", err)
	}
}


/*方法实现*/

// 保存
func PutEntity(stub shim.ChaincodeStubInterface, entity BlockDataEntity) ([]byte, bool) {
	entity.ObjectType = DOC_TYPE
	b, err := json.Marshal(entity)
	if err != nil {
		return nil, false
	}
	// 保存edu状态
	err = stub.PutState(entity.Id+entity.Type, b)

	if err != nil {
		return nil, false
	}
	return b, true
}

//根据类别查询信息
func (t *CommonBlockChaincode) queryByType(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	infoType := args[0]
	// 拼装CouchDB所需要的查询字符串(是标准的一个JSON串)
	// queryString := fmt.Sprintf("{\"selector\":{\"docType\":\"eduObj\", \"CertNo\":\"%s\"}}", CertNo)
	queryString := fmt.Sprintf("{\"selector\":{\"docType\":\"%s\", \"Type\":\"%s\"}}",DOC_TYPE , infoType)

	// 查询数据
	result, err := getEntityByQueryString(stub, queryString)
	if err != nil {
		return shim.Error("根据信息类别查询发生错误")
	}
	if result == nil {
		return shim.Error("根据指定类别没有查询到相关的信息")
	}
	return shim.Success(result)
}

// 根据主键和类别、创建时间查询指定信息
func (t *CommonBlockChaincode) queryInfoByIdAndTypeAndDate(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 1 {
		return shim.Error("给定的参数个数不符合要求！")
	}
	var entity BlockDataEntity                      //定义一个教育对象
	err := json.Unmarshal([]byte(args[0]), &entity) //将接收到的字符串转换成对象
	if err != nil {
		return shim.Error("反序列化信息时发生错误")
	}

	// 拼装CouchDB所需要的查询字符串(是标准的一个JSON串)
	// queryString := fmt.Sprintf("{\"selector\":{\"docType\":\"eduObj\", \"CertNo\":\"%s\"}}", CertNo)
	queryString := fmt.Sprintf("{\"selector\":{\"docType\":\"%s\", \"CardNo\":\"%s\", \"Type\":\"%s\", \"CreatedDate\":\"%s\"}}", DOC_TYPE, entity.Id, entity.Type, entity.CreatedDate)

	// 查询数据
	result, err := getEntityByQueryString(stub, queryString)
	if err != nil {
		return shim.Error("根据编号及类别、日期查询信息时发生错误:"+queryString)
	}
	if result == nil {
		return shim.Error("根据指定编号及类别没有查询到相关的信息")
	}
	return shim.Success(result)
}

//查询唯一的区块信息
func (t *CommonBlockChaincode) queryUnique(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 1 {
		return shim.Error("给定的参数个数不符合要求!")
	}
	var param BlockDataEntity                      //需要查询的参数对象
	err := json.Unmarshal([]byte(args[0]), &param) //将接收到的字符串转换成对象
	if err != nil {
		return shim.Error("反序列化信息时发生错误")
	}
	var createdDate = param.CreatedDate //取出当前对象的创建日期
	// 根据唯一key查询edu状态
	b, err := stub.GetState(args[0]+args[1])
	if err != nil {
		return shim.Error("根据主键和类型查询信息失败")
	}
	if b == nil {
		return shim.Error("根据主键没有查询到"+args[1]+"类型相关数据")
	}
	// 对查询到的状态进行反序列化
	var entity BlockDataEntity
	err = json.Unmarshal(b, &entity)
	if err != nil {
		return  shim.Error("反序列化对象信息失败")
	}

	//当前是否是返回的数据
	if entity.CreatedDate == createdDate{
		result, err := json.Marshal(entity)
		if err != nil {
			return shim.Error("序列化对象时发生错误")
		}
		return shim.Success(result)
	}
	//不是当前数据时，寻找唯一的数据
	var resultByte []byte = nil
	// 获取历史变更数据(id + type)
	iterator, err := stub.GetHistoryForKey(entity.Id+ entity.Type)
	if err != nil {
		return shim.Error("根据指定的主键和类型查询对应的历史变更数据失败")
	}
	defer iterator.Close()
	// 迭代处理：查询指定信息
	var hisEntity BlockDataEntity
	for iterator.HasNext() {
		hisData, err := iterator.Next()
		if err != nil {
			return shim.Error("获取历史变更数据失败")
		}
		var historyItem HistoryItem
		historyItem.TxId = hisData.TxId
		_ = json.Unmarshal(hisData.Value, &hisEntity)

		if hisData.Value != nil {
			if hisEntity.CreatedDate == createdDate{
				result, err := json.Marshal(hisEntity)
				if err != nil {
					return shim.Error("序列化对象信息时发生错误")
				}
				resultByte = result
				break
			}
		}
	}

	return shim.Success(resultByte)
}


// 根据组合主键（id + type）查询状态
func GetEntityInfo(stub shim.ChaincodeStubInterface, args []string) (BlockDataEntity, bool)  {
	var entity BlockDataEntity
	b, err := stub.GetState(args[0]+args[1])//通过唯一主键(key+type构成唯一主键)获取状态
	//b, err := stub.GetStateByPartialCompositeKey(DOC_TYPE,args)//通过组合键获取状态

	if err != nil {
		return entity, false
	}

	if b == nil {
		return entity, false
	}

	// 对查询到的状态进行反序列化
	err = json.Unmarshal(b, &entity)
	if err != nil {
		return entity, false
	}
	// 返回结果
	return entity, true
}

// 根据指定的查询字符串实现富查询
func getEntityByQueryString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer  resultsIterator.Close()

	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}

		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		bArrayMemberAlreadyWritten = true
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil

}

// 添加信息
// args: educationObject
// id+type为 key, BlockDataEntity 为 value
func (t *CommonBlockChaincode) add(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 2{
		return shim.Error("给定的参数个数不符合要求!")
	}
	var entity BlockDataEntity                      //定义一个对象
	err := json.Unmarshal([]byte(args[0]), &entity) //将接收到的字符串转换成对象
	if err != nil {
		return shim.Error("反序列化信息时发生错误")
	}
	// 查重: key必须唯一（id+type）
	// 定义一个参数数组
	var params []string = []string{entity.Id, entity.Type}
	//调用查询
	_, exist := GetEntityInfo(stub,params)
	if exist {
		return shim.Error("要添加"+ entity.Type+"类型的当前对象已经存在！")
	}
	_, bl := PutEntity(stub, entity)
	if !bl {
		return shim.Error("保存对象信息时发生错误")
	}
	err = stub.SetEvent(args[1], []byte{})
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success([]byte("对象信息添加成功"))
}

// 根据key+type查询详情（溯源）
func (t *CommonBlockChaincode) queryInfoByIdAndType(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 2 {
		return shim.Error("给定的参数个数不符合要求：id + type")
	}
	// 根据唯一key查询edu状态
	b, err := stub.GetState(args[0]+args[1])
	if err != nil {
		return shim.Error("根据主键和类型查询信息失败")
	}
	if b == nil {
		return shim.Error("根据当前主键没有查询到"+args[1]+"类型相关数据")
	}
	// 对查询到的状态进行反序列化
	var entity BlockDataEntity
	err = json.Unmarshal(b, &entity)
	if err != nil {
		return  shim.Error("反序列化对象信息失败")
	}
	// 获取历史变更数据(id + type)
	iterator, err := stub.GetHistoryForKey(entity.Id+ entity.Type)
	if err != nil {
		return shim.Error("根据指定的主键查询历史数据失败")
	}
	defer iterator.Close()
	// 迭代处理：封装查询到的单条信息
	var historys []HistoryItem
	var hisEntity BlockDataEntity
	for iterator.HasNext() {
		hisData, err := iterator.Next()
		if err != nil {
			return shim.Error("获取历史变更数据失败")
		}
		var historyItem HistoryItem
		historyItem.TxId = hisData.TxId
		_ = json.Unmarshal(hisData.Value, &hisEntity)

		if hisData.Value == nil {
			var empty BlockDataEntity
			historyItem.BlockDataEntity = empty
		}else {
			historyItem.BlockDataEntity = hisEntity
		}
		historys = append(historys, historyItem)
	}
	entity.Historys = historys
	// 返回
	result, err := json.Marshal(entity)
	if err != nil {
		return shim.Error("序列化对象信息时发生错误")
	}
	return shim.Success(result)
}

// 根据id + key 更新信息
func (t *CommonBlockChaincode) update(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	if len(args) != 2{
		return shim.Error("给定的参数个数不符合要求！")
	}

	var info BlockDataEntity //接收的最新对象
	err := json.Unmarshal([]byte(args[0]), &info)
	if err != nil {
		return  shim.Error("反序列化对象信息失败")
	}

	// 根据唯一组合主键查询信息
	var params []string = []string{info.Id,info.Type}
	result, bl := GetEntityInfo(stub,params)
	if !bl{
		return shim.Error("根据主键查询当前"+info.Type+"类型的信息时发生错误")
	}

	result.Type = info.Type
	result.Content = info.Content
	result.Id = info.Id
	result.CreatedDate = info.CreatedDate
	result.ObjectType = info.ObjectType

	//保存信息
	_, bl = PutEntity(stub, result)
	if !bl {
		return shim.Error("保存对象时发生错误")
	}

	err = stub.SetEvent(args[1], []byte{})
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success([]byte("信息更新成功"))
}
