# Kiribi-IO
Provides classes and interfaces to read and write objects using the Kiribi IO Framework.

### Introduction
Provides classes and interfaces to read and write objects using the Kiribi IO Framework.

### Features
* Highly compact serialization.
* VarInt support.
* Byte array utilities.

### Overview
Provides classes and interfaces to read and write objects using the Kiribi IO Framework.

##### Encodable Objects
Defines the contract to be encodable. Encodable objects must implement the Encodable interface and provide a constructor which takes a VarInput as a single argument. Note that no class meta-data is serialized. It is up to the developer to know which classes are being decoded.

##### VarInput
Interface for reading Encodable objects. VarInts and serveral other data types are also supported.

##### VarOutput
Interface for writing Encodable objects. VarInts and serveral other data types are also supported.

##### ByteUtils
Various static utility methods for byte arrays.

### Code Example

 	public Class Foo implements Encodeable {
    	public Foo() {}
 
    	public Foo(VarInput in) throws IOException {
          	...
        }
 
        @Override
        public void write(VarOutput out) throws IOException {
          	...
        }
        ...
    }
 
    ....
 
    VarInput in = ...
    Foo foo = in.read(Foo::new);
    
    VarOutput out = ...
    foo.write(out);


### Module Dependencies
##### Requires
* java.base

##### Exports
* rs.igram.kiribi.io

### To Do
* Determine minimum supported Java version.