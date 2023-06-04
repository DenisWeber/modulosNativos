import React, { useState } from 'react';
import { View, Text, Modal, TouchableOpacity, StyleSheet } from 'react-native';

export const PermissionModal = ({ visible, onRequestClose }) => {
    return (
      <Modal
        visible={visible}
        transparent
        animationType="fade"
        onRequestClose={onRequestClose}
      >
        <View style={styles.container}>
          <View style={styles.modalContent}>
            <Text style={styles.title}>Permission Request</Text>
            <Text style={styles.description}>
              This app requires your permission to access certain features.
            </Text>
            <TouchableOpacity style={styles.button} onPress={onRequestClose}>
              <Text style={styles.buttonText}>Allow</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.button} onPress={onRequestClose}>
              <Text style={styles.buttonText}>Deny</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    );
  };
  
  const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContent: {
      backgroundColor: 'white',
      borderRadius: 8,
      padding: 16,
      width: '80%',
    },
    title: {
      fontSize: 18,
      fontWeight: 'bold',
      marginBottom: 8,
    },
    description: {
      marginBottom: 16,
    },
    button: {
      backgroundColor: '#2196F3',
      paddingVertical: 12,
      borderRadius: 4,
      marginBottom: 8,
    },
    buttonText: {
      color: 'white',
      fontSize: 16,
      textAlign: 'center',
    },
  });
  