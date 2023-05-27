import React from 'react';
import { useEffect, useState } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  View,
  TouchableOpacity,
  Text,
  ScrollView,
  NativeEventEmitter,
  NativeModules,
  TextInput
} from 'react-native';
import ReactNativeStartService from 'react-native-start-service';

const App = () => {
  const [logs, setLogs] = useState([]);
  const [numero, setNumero] = useState('');
  const [mensagem, setMensagem] = useState('');

  const NOME_PACOTE = 'com.modulosnativos';
  const SERVICO_CLASSE = 'com.modulosnativos.LogServicoService';
  const ACAO_WHATSAPP = "com.modulosnativos.ACAO_WHATSAPP";
  const ACAO_TELEFONE = "com.modulosnativos.ACAO_TELEFONE";

  useEffect(() => {
    const logModule = NativeModules.LogServico;
    const eventEmitter = new NativeEventEmitter(logModule);
    const subscription = eventEmitter.addListener('log', (event) => {
      addLog(event.message);
    });

    NativeModules.LogServico.mostrarLogs();

    return () => {
      subscription.remove();
    };
  }, []);

  const addLog = (message) => {
    setLogs(prevLogs => [...prevLogs, message]);
  };
  

  const clickHandlerWhatsapp = () => {
    NativeModules.LogServico.chamarWhatsapp(numero, mensagem);
  };

  const clickHandlerWhatsappStartService = async () => {
    const resultado = await ReactNativeStartService.startAsync(NOME_PACOTE, SERVICO_CLASSE, ACAO_WHATSAPP, {numero, mensagem});

    setLogs([...logs, resultado.message]);
  };

  const clickHandlerLigar = () => {
    NativeModules.LogServico.chamarLigacao(numero);
  };

  const clickHandlerLigarStartService = async () => {
    const resultado = await ReactNativeStartService.startAsync(NOME_PACOTE, SERVICO_CLASSE, ACAO_TELEFONE, {numero});

    console.log(resultado.message);
    
    setLogs([...logs, resultado.message]);
  };

  const clickHandlerDeletarLogs = () => {
    NativeModules.LogServico.deletarLogs();
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.container}>
        <TextInput 
          style={styles.textStyle}
          onChangeText={(valor) => setNumero(valor)}
          value={numero}
          placeholder='Digite o número'/>
        <TextInput 
          style={styles.textStyle}
          onChangeText={(valor) => setMensagem(valor)}
          value={mensagem}
          placeholder='Digite a mensagem'/>
        <View style={styles.viewBotoes}>
          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerWhatsapp}
            style={styles.botaoWhatsapp}>
              <Text style={styles.textoBotoes}>Whatsapp</Text>
          </TouchableOpacity>

          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerLigar}
            style={styles.botaoLigar}>
              <Text style={styles.textoBotoes}>Ligar</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.viewBotoes}>
          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerWhatsappStartService}
            style={styles.botaoWhatsapp}>
              <Text style={styles.textoBotoes}>Whatsapp Service</Text>
          </TouchableOpacity>

          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerLigarStartService}
            style={styles.botaoLigar}>
              <Text style={styles.textoBotoes}>Ligar Service</Text>
          </TouchableOpacity>
        </View>

        {/* <View style={styles.viewBotoes}>
          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerDeletarLogs}
            style={styles.botaoDeletar}>
              <Text style={styles.textoBotoes}>Deletar</Text>
          </TouchableOpacity>
        </View> */}
        <ScrollView style={styles.scrollView}>
          {logs.map((log, index) => (
          <Text style={styles.textLogs} key={index}>{log}</Text>
        ))}
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

export default App;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    padding: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  titleStyle: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center',
    padding: 10,
  },
  textStyle: {
    fontSize: 16,
    textAlign: 'center',
    padding: 10,
    borderWidth: 1,
    width: 300,
    marginBottom: 10
  },
  botaoWhatsapp: {
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'green',
    borderRadius: 2
  },
  botaoParar: {
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'red',
    borderRadius: 2
  },
  botaoLigar: {
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'blue',
    borderRadius: 2
  },
  botaoDeletar: {
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'red',
    borderRadius: 2
  },
  botaoLogs: {
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'blue'
  },
  textoBotoes: {
    color: '#fff'
  },
  floatingButtonStyle: {
    resizeMode: 'contain',
    width: 70,
    height: 70,
  },
  scrollView: {
    borderWidth: 1,
    width: 340
  },
  textLogs: {
    paddingLeft: 5
  },
  viewBotoes: {
    flexDirection: 'row',
    marginBottom: 5,
    justifyContent: 'space-between',
    width: '70%'
  }
});
