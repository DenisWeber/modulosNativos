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
  NativeModules
} from 'react-native';

const App = () => {
  const [logs, setLogs] = useState([]);

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
  

  const clickHandlerIniciar = () => {
    NativeModules.LogServico.iniciarServico();
  };

  const clickHandlerParar = () => {
    NativeModules.LogServico.pararServico();
  };

  const clickHandlerDeletarLogs = () => {
    NativeModules.LogServico.deletarLogs();
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.container}>
        <Text style={styles.textStyle}>
          Clique no Botão para Ver o Alerta
        </Text>
        <View style={styles.viewBotoes}>
          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerIniciar}
            style={styles.botaoIniciar}>
              <Text style={styles.textoBotoes}>Iniciar</Text>
          </TouchableOpacity>

          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerParar}
            style={styles.botaoParar}>
              <Text style={styles.textoBotoes}>Parar</Text>
          </TouchableOpacity>

          <TouchableOpacity
            activeOpacity={0.7}
            onPress={clickHandlerDeletarLogs}
            style={styles.botaoDeletar}>
              <Text style={styles.textoBotoes}>Deletar</Text>
          </TouchableOpacity>
        </View>
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
  },
  botaoIniciar: {
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
  botaoDeletar: {
    width: 110,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'blue',
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
